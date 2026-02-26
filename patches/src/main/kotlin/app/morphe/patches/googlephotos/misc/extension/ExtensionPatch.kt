package app.morphe.patches.googlephotos.misc.extension

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val extensionPatch = bytecodePatch(
    name = "Extension",
    description = "Extension for Google Photos patches.",
) {
    // Extension hooks can be added here if needed.
}
