package app.template.patches.skat_patch

import app.morphe.patcher.Fingerprint

object ShowFullscreenAdFingerprint : Fingerprint(
    definingClass = "Lcom/isarinteractive/GameActivity;",
    name = "showFullscreenAd",
    returnType = "Z",
    parameters = emptyList()
)

object ShowBannerAdFingerprint : Fingerprint(
    definingClass = "Lcom/isarinteractive/GameActivity;",
    name = "showBannerAd",
    returnType = "V",
    parameters = emptyList()
)

object HideBannerAdFingerprint : Fingerprint(
    definingClass = "Lcom/isarinteractive/GameActivity;",
    name = "hideBannerAd",
    returnType = "V",
    parameters = emptyList()
)
