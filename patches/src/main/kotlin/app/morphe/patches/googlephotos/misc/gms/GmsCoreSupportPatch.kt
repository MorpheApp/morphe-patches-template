package app.morphe.patches.googlephotos.misc.gms

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.googlephotos.misc.extension.extensionPatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportResourcePatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = PHOTOS_PACKAGE_NAME,
    toPackageName = MORPHE_PHOTOS_PACKAGE_NAME,
    mainActivityOnCreateFingerprint = homeActivityOnCreateFingerprint,
    extensionPatch = extensionPatch,
    gmsCoreSupportResourcePatchFactory = {
        gmsCoreSupportResourcePatch(
            fromPackageName = PHOTOS_PACKAGE_NAME,
            toPackageName = MORPHE_PHOTOS_PACKAGE_NAME,
            spoofedPackageSignature = "..."
        )
    },
) {
    compatibleWith(PHOTOS_PACKAGE_NAME)
}
