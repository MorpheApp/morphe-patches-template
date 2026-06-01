package kiraio.morphe.androidproxyserver.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Android Proxy Server",
        packageName = "cn.adonet.proxyevery",
        appIconColor = 0X01aa68,
        targets = listOf(AppTarget("9.9"))
    )
}