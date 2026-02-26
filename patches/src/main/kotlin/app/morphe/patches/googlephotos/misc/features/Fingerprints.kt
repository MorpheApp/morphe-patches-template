package app.morphe.patches.googlephotos.misc.features

import app.morphe.patcher.fingerprint

internal val initializeFeaturesEnumFingerprint = fingerprint {
    strings("com.google.android.apps.photos.NEXUS_PRELOAD")
}
