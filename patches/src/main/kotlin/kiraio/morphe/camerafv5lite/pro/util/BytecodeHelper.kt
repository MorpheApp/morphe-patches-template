package kiraio.morphe.camerafv5lite.pro.util

import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t

object BytecodeHelper {
    fun findNextIfEqz(
        instructions: List<Instruction>,
        startIndex: Int
    ): Pair<Int, Int> {

        val relativeIndex =
            instructions
                .drop(startIndex)
                .indexOfFirst { it.opcode == Opcode.IF_EQZ }

        require(relativeIndex != -1) {
            "Unable to find IF_EQZ after index $startIndex"
        }

        val index = startIndex + relativeIndex

        val register =
            (instructions[index] as Instruction21t).registerA

        return index to register
    }
}
