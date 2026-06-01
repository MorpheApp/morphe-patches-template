package kiraio.morphe.androidproxyserver.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstructionReversedOrThrow
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import kiraio.morphe.androidproxyserver.shared.Constants

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable Ads",
    description = "Disable banner, splash, and rewarded ads.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)
    execute {
        MainFragmentAdsFingerprint.method.returnEarly()
        MainActivityMenuFingerprint.method.addInstructions(
            MainActivityMenuFingerprint.method.indexOfFirstInstructionReversedOrThrow(Opcode.IPUT_OBJECT) + 1,
            """
                const v0, 0x7f090037
                invoke-interface {p1, v0}, Landroid/view/Menu;->removeItem(I)V
            """.trimIndent()
        )
    }
}
