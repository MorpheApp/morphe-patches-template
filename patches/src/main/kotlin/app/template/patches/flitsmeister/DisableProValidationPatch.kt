package app.morphe.patches.flitsmeister

import app.morphe.patcher.fingerprint.Fingerprint
import app.morphe.patcher.fingerprint.instructionFilter.filters.string
import app.morphe.patcher.patch.bytecodePatch

// ── Fingerprints ─────────────────────────────────────────────────────────────

/**
 * Matches the method in InAppPurchaseUtil that validates the pro subscription
 * against Google Play Billing. The method logs "[IAP] Pro subscription updated: [valid ..."
 * when a receipt is validated.
 *
 * By preventing this method from running its normal logic, the app will never
 * reset the pro status back to false after a server-side check fails.
 */
object ProSubscriptionValidationFingerprint : Fingerprint(
    filters = listOf(
        string("[IAP] Pro subscription updated: [valid "),
    ),
)

/**
 * Matches updateProSubscriptionData in InAppPurchaseUtil, which is responsible
 * for writing the subscription data to SharedPreferences (pref_fm_pro_active, etc).
 */
object UpdateProSubscriptionDataFingerprint : Fingerprint(
    filters = listOf(
        string("[IAP] Updated PRO values: [isProActive "),
    ),
)

// ── Patch ────────────────────────────────────────────────────────────────────

@Suppress("unused")
val disableProValidationPatch = bytecodePatch(
    name = "Disable Pro validation",
    description = "Prevents the app from re-validating the Pro subscription against " +
        "Google Play Billing, so the patched pro status is never overwritten.",
    default = true,
) {
    compatibleWith(FLITSMEISTER_COMPATIBILITY)

    // Run after the main pro unlock so that pro is already activated.
    dependsOn(unlockFlitsmeisterProPatch)

    execute {
        // ── 1. Neuter the subscription validation method ─────────────────
        //    Make it return immediately without performing the server-side check.
        ProSubscriptionValidationFingerprint.methodOrNull?.let { method ->
            method.addInstructions(
                0,
                """
                    return-void
                """,
            )
        }

        // ── 2. Neuter updateProSubscriptionData ──────────────────────────
        //    Prevent the app from overwriting our patched SharedPreferences
        //    with the real (non-pro) subscription data from the server.
        UpdateProSubscriptionDataFingerprint.methodOrNull?.let { method ->
            method.addInstructions(
                0,
                """
                    return-void
                """,
            )
        }
    }
}
