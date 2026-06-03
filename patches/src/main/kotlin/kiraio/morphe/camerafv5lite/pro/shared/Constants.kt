package kiraio.morphe.camerafv5lite.pro.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Camera FV-5 Lite",
        packageName = "com.flavionet.android.camera.lite",
        appIconColor = 0X161616,
        targets = listOf(AppTarget("5.4.0"))
    )
}