package app.morphe.patches.googlephotos.misc.backup

import app.morphe.patcher.fingerprint

internal val isDCIMFolderBackupControlDisabled = fingerprint {
    returns("Z")
    parameters("L")
    strings("DCIM folder backup control is disabled")
}
