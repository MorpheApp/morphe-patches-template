package app.morphe.patches.flitsmeister

import app.morphe.patcher.fingerprint.Fingerprint
import app.morphe.patcher.fingerprint.instructionFilter.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fingerprint.instructionFilter.filters.methodCall
import app.morphe.patcher.fingerprint.instructionFilter.filters.opcode
import app.morphe.patcher.fingerprint.instructionFilter.filters.string
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

// ── Fingerprints for individual Pro feature gates ────────────────────────────

/**
 * DefaultProFeatureProvider$matrixSignEnabled — highway electronic matrix signs.
 */
object MatrixSignEnabledFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("pref_fm_pro_active"),
        methodCall(name = "getBoolean", returnType = "Z"),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
    ),
)

/**
 * Catches any boolean method in DefaultProFeatureProvider that checks pro status
 * for individual features. These methods all follow the same pattern:
 *   read isPro → return isPro
 *
 * We find them by their defining class using the classDef from the main pro fingerprint,
 * then override all boolean-returning methods to return true.
 */
object ProFeatureProviderClassFingerprint : Fingerprint(
    filters = listOf(
        string("pref_fm_pro_active"),
    ),
)

// ── Patch ────────────────────────────────────────────────────────────────────

@Suppress("unused")
val unlockProFeaturesPatch = bytecodePatch(
    name = "Unlock Pro features",
    description = "Forces all individual Pro feature gates " +
        "(matrix signs, actual speed, traffic lights, Bluetooth autostart, " +
        "speed check warnings, vibration) to return enabled.",
    default = true,
) {
    compatibleWith(FLITSMEISTER_COMPATIBILITY)

    // Run after the main pro patch so that fingerprint class resolution is shared.
    dependsOn(unlockFlitsmeisterProPatch)

    execute {
        // Find the DefaultProFeatureProvider class using a fingerprint
        // that matches any method containing "pref_fm_pro_active" string.
        //
        // Then iterate every method in that class that returns boolean (Z)
        // and inject "return true" at the top.
        val providerClass = ProFeatureProviderClassFingerprint.classDef

        providerClass.methods.forEach { method ->
            if (method.returnType == "Z" && method.implementation != null) {
                method.addInstructions(
                    0,
                    """
                        const/4 v0, 0x1
                        return v0
                    """,
                )
            }
        }
    }
}
