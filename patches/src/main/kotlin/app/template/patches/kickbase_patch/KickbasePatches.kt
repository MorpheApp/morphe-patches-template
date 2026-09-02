package app.template.patches.kickbase_patch

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_KICKBASE

@Suppress("unused")
val disableKickbaseAdsPatch = bytecodePatch(
    name = "Disable Kickbase Ads",
    description = "Disables banner ads, full-screen ads, and in-feed ads in Kickbase.",
    default = true
) {
    compatibleWith(COMPATIBILITY_KICKBASE)

    execute {
        // Intercept Google AdMob BaseAdView.loadAd()
        BaseAdViewLoadAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )

        // Intercept AddApptr BannerPlacementLayout.presentAd() -> return immediately
        AddApptrPresentAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}
