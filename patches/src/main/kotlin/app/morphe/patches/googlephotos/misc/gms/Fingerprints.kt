package app.morphe.patches.googlephotos.misc.gms

import app.morphe.patcher.fingerprint

internal val homeActivityOnCreateFingerprint = fingerprint {
    returns("V")
    parameters("Landroid/os/Bundle;")
    strings("HomeActivity")
}
