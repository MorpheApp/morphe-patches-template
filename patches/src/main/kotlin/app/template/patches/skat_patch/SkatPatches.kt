package app.template.patches.skat_patch

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_SKAT

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable Ads",
    description = "Disables banner and fullscreen ads, and automatically signals ad completion to the game engine.",
    default = true
) {
    compatibleWith(COMPATIBILITY_SKAT)

    execute {
        // Intercept fullscreen ad: notify game engine that ad has closed, then return true
        ShowFullscreenAdFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Lcom/isarinteractive/NativeCallHelper;->callFullscreenAdClosed()V
                const/4 v0, 0x1
                return v0
            """
        )

        // Intercept banner ad: do nothing, return immediately
        ShowBannerAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )

        // Intercept hide banner ad: do nothing, return immediately
        HideBannerAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}
