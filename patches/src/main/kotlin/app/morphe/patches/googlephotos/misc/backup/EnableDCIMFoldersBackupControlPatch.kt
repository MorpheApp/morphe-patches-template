package app.morphe.patches.googlephotos.misc.backup

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableDCIMFoldersBackupControlPatch = bytecodePatch(
    name = "Enable DCIM folders backup control",
    description = "Disables always on backup for the Camera and other DCIM folders, allowing you to control backup for each folder individually.",
    use = false,
) {
    compatibleWith("com.google.android.apps.photos")

    execute {
        isDCIMFolderBackupControlDisabled.method.returnEarly(false)
    }
}
