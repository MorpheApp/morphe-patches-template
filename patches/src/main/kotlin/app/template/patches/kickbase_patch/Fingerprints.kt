package app.template.patches.kickbase_patch

import app.morphe.patcher.Fingerprint

object BaseAdViewLoadAdFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/ads/BaseAdView;",
    name = "loadAd",
    returnType = "V",
    parameters = listOf("Lcom/google/android/gms/ads/AdRequest;")
)

object AddApptrPresentAdFingerprint : Fingerprint(
    definingClass = "Lcom/intentsoftware/addapptr/BannerPlacementLayout;",
    name = "presentAd\$AATKit_release",
    returnType = "V",
    parameters = listOf("Lcom/intentsoftware/addapptr/internal/ad/BannerAd;")
)
