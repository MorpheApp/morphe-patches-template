package app.morphe.patches.flitsmeister

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

val FLITSMEISTER_COMPATIBILITY = Compatibility(
    name = "Flitsmeister",
    packageName = "nl.flitsmeister",
    appIconColor = 0x2094F4,
    targets = listOf(
        AppTarget(
            version = "14.2.1",
        ),
    ),
)
