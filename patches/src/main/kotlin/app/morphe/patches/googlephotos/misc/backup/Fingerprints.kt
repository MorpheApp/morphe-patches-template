package app.morphe.patches.googlephotos.misc.backup

import app.morphe.patcher.fingerprint

internal val isDCIMFolderBackupControlDisabled = fingerprint {
    returns("Z")
    strings("/dcim", "/mars_files/")
}
