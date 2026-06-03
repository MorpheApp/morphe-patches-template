package kiraio.morphe.camerafv5lite.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

object ImageResolutionFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    returnType = "V",
    filters = listOf(
        string("capabilities.supportedPictureSizes"),
        string("p_buy_pro")
    ),
)

object RawCaptureFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    returnType = "V",
    filters = listOf(
        methodCall(
            definingClass = "Landroidx/preference/Preference;",
            name = "setEnabled"
        ),
        string("p_buy_pro_raw")
    )
)

object BuyProRawCaptureFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PROTECTED),
    returnType = "V",
    filters = listOf(
        string("p_buy_pro_raw"),
    )
)
