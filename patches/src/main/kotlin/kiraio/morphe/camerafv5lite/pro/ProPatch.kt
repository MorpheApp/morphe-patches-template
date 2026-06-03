package kiraio.morphe.camerafv5lite.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstructionReversed
import app.morphe.util.insertLiteralOverride
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import kiraio.morphe.camerafv5lite.pro.shared.Constants

@Suppress("unused")
val unlockImageResolutionPatch = bytecodePatch(
    name = "Higher Image Resolution",
    description = "Unlock higher image resolutions that the device support.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)
    execute {
        val method = ImageResolutionFingerprint.method
        val firstMatchIndex =
            ImageResolutionFingerprint.instructionMatches.first().index
        val lastMatchIndex =
            ImageResolutionFingerprint.instructionMatches.last().index

        val firstIfIndex =
            method.indexOfFirstInstructionReversed(
                firstMatchIndex,
                Opcode.IF_EQZ
            )
        val lastIfIndex =
            method.indexOfFirstInstructionReversed(
                lastMatchIndex,
                Opcode.IF_EQZ
            )

        data class Patch(
            val insertIndex: Int,
            val value: Boolean,
        )

        listOf(
            Patch(
                insertIndex = firstIfIndex - 3,
                value = false,
            ),
            Patch(
                insertIndex = lastIfIndex - 3,
                value = true,
            ),
        )
            .sortedByDescending { it.insertIndex }
            .forEach { patch ->
                method.insertLiteralOverride(patch.insertIndex, patch.value)
            }
    }
}

@Suppress("unused")
val unlockRawCapturePatch = bytecodePatch(
    name = "Enable Raw Capture",
    description = "Force enable DNG raw capture option.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)
    execute {
        val rawCaptureMethod = RawCaptureFingerprint.method
        val rawCaptureFirstIndex =
            RawCaptureFingerprint.instructionMatches.first().index
        val rawCaptureLastIndex =
            RawCaptureFingerprint.instructionMatches.last().index

        // Early return to hide buy pro or it's lite version button
        rawCaptureMethod.addInstruction(
            rawCaptureLastIndex,
            "return-void"
        )

        // Force enable DNG raw capture
        val enableRawCaptureRegister = rawCaptureMethod.getInstruction<Instruction35c>(rawCaptureFirstIndex).registerG
        rawCaptureMethod.addInstruction(
            rawCaptureFirstIndex,
            "const/4 v$enableRawCaptureRegister, 0x1"
        )

        // Further hide buy pro version button
        val buyProMethod = BuyProRawCaptureFingerprint.method
        val buyProIndex =
            RawCaptureFingerprint.instructionMatches.first().index
        buyProMethod.insertLiteralOverride(
            buyProIndex - 3,
            true
        )
    }
}
