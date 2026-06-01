package kiraio.morphe.androidproxyserver.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object MainFragmentAdsFingerprint : Fingerprint(
    definingClass = "Lcn/adonet/proxyevery/MainFragment;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            type = "Ljava/util/ArrayList;"
        )
    ),
)

object MainActivityMenuFingerprint : Fingerprint(
    definingClass = "Lcn/adonet/proxyevery/MainActivity;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf("Landroid/view/Menu;"),
    custom = { method, _ -> method.name == "onCreateOptionsMenu" },
)
