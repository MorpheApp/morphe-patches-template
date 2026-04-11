package app.morphe.patches.flitsmeister

import app.morphe.patcher.fingerprint.Fingerprint
import app.morphe.patcher.fingerprint.instructionFilter.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fingerprint.instructionFilter.filters.fieldAccess
import app.morphe.patcher.fingerprint.instructionFilter.filters.methodCall
import app.morphe.patcher.fingerprint.instructionFilter.filters.opcode
import app.morphe.patcher.fingerprint.instructionFilter.filters.string
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.findInstructionIndicesReversedOrThrow
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

// ── Fingerprints ─────────────────────────────────────────────────────────────

/**
 * Matches FlitsmeisterProUtil's method that checks whether Pro is currently active.
 * The method reads SharedPreferences key "pref_fm_pro_active" and returns a boolean.
 *
 * Log line reference: "[IAP] Updated PRO values: [isProActive ..."
 */
object IsProActiveFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("pref_fm_pro_active"),
        methodCall(
            name = "getBoolean",
            returnType = "Z",
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
    ),
)

/**
 * Matches the debug override check that reads "pref_fm_pro_active_debug".
 * Patching this as a fallback in case the app checks the debug pref separately.
 */
object IsProActiveDebugFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("pref_fm_pro_active_debug"),
        methodCall(
            name = "getBoolean",
            returnType = "Z",
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
    ),
)

/**
 * Matches the method that checks whether Pro subscription is active.
 * Reads "pref_pro_subscription_active" from SharedPreferences.
 */
object IsProSubscriptionActiveFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("pref_pro_subscription_active"),
        methodCall(
            name = "getBoolean",
            returnType = "Z",
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
    ),
)

/**
 * Matches rememberIsPro composable in ProStatus.kt (line 24).
 * This feeds the isPro value to every UI state.
 */
object RememberIsProFingerprint : Fingerprint(
    filters = listOf(
        string("pref_fm_pro_active"),
    ),
)

/**
 * Matches the ProType check — targets the method that reads "pref_pro_subscription_tier"
 * to determine whether user is Start, Pro, or ProPlus.
 */
object ProSubscriptionTierFingerprint : Fingerprint(
    filters = listOf(
        string("pref_pro_subscription_tier"),
    ),
)

// ── Patch ────────────────────────────────────────────────────────────────────

@Suppress("unused")
val unlockFlitsmeisterProPatch = bytecodePatch(
    name = "Unlock Flitsmeister Pro",
    description = "Unlocks Flitsmeister Pro and Pro+ features by patching the pro status checks to always return true.",
    default = true,
) {
    compatibleWith(FLITSMEISTER_COMPATIBILITY)

    execute {
        // ── 1. Patch isProActive → always return true ────────────────────
        IsProActiveFingerprint.let {
            val moveResultMatch = it.instructionMatches[2]
            val moveResultIndex = moveResultMatch.index
            val register = moveResultMatch.getInstruction<OneRegisterInstruction>().registerA

            it.method.addInstructions(
                moveResultIndex + 1,
                """
                    const/4 v$register, 0x1
                """,
            )
        }

        // ── 2. Patch isProActiveDebug → always return true ───────────────
        IsProActiveDebugFingerprint.methodOrNull?.let { method ->
            val match = IsProActiveDebugFingerprint.instructionMatches[2]
            val index = match.index
            val register = match.getInstruction<OneRegisterInstruction>().registerA

            method.addInstructions(
                index + 1,
                """
                    const/4 v$register, 0x1
                """,
            )
        }

        // ── 3. Patch isProSubscriptionActive → always return true ────────
        IsProSubscriptionActiveFingerprint.methodOrNull?.let { method ->
            val match = IsProSubscriptionActiveFingerprint.instructionMatches[2]
            val index = match.index
            val register = match.getInstruction<OneRegisterInstruction>().registerA

            method.addInstructions(
                index + 1,
                """
                    const/4 v$register, 0x1
                """,
            )
        }

        // ── 4. Force entire pro-status method to return true early ───────
        //    Overwrite the very beginning of the method found by IsProActiveFingerprint
        //    to unconditionally return true. This is a belt-and-suspenders approach:
        //    even if the SharedPreferences patching above is circumvented by
        //    additional server-side validation, this hard return will take effect.
        IsProActiveFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """,
        )
    }
}
