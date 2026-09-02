package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_SKAT = Compatibility(
        name = "Skat",
        packageName = "com.isarinteractive.skat.android.ad",
        apkFileType = ApkFileType.XAPK,
        appIconColor = 0x2E7D32,
        targets = listOf(
            AppTarget(
                version = "19.2.1"
            ),
            AppTarget(
                version = null,
                isExperimental = true
            )
        )
    )

    val COMPATIBILITY_KICKBASE = Compatibility(
        name = "Kickbase",
        packageName = "com.kkstr.bundesliga",
        apkFileType = ApkFileType.APKM,
        appIconColor = 0xE1001A,
        targets = listOf(
            AppTarget(
                version = "4.8.3"
            ),
            AppTarget(
                version = null,
                isExperimental = true
            )
        )
    )
}
