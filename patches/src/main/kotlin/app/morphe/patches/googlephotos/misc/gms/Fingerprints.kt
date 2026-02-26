package app.morphe.patches.googlephotos.misc.gms

import app.morphe.patcher.fingerprint

internal val homeActivityOnCreateFingerprint = fingerprint {
    custom { methodDef, classDef ->
        methodDef.name == "onCreate" && classDef.endsWith("/HomeActivity;")
    }
}
