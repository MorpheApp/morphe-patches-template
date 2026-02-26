/*
 * Copyright 2025 Morphe.
 * https://github.com/morpheapp/morphe-patches
 *
 * File-Specific License Notice (GPLv3 Section 7 Additional Permission).
 *
 * This file is part of the Morphe patches project and is licensed under
 * the GNU General Public License version 3 (GPLv3), with the Additional
 * Terms under Section 7 described in the Morphe patches LICENSE file.
 *
 * https://www.gnu.org/licenses/gpl-3.0.html
 */

package app.morphe.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.Reference

/**
 * Get the [Reference] of an [Instruction] as [T].
 *
 * @param T The type of [Reference] to cast to.
 * @return The [Reference] as [T] or null
 * if the [Instruction] is not a [ReferenceInstruction] or the [Reference] is not of type [T].
 */
inline fun <reified T : Reference> Instruction.getReference() = (this as? ReferenceInstruction)?.reference as? T

private const val RETURN_TYPE_MISMATCH = "Mismatch between override type and Method return type"

/**
 * Overrides the first instruction of a method with a return-void instruction.
 * None of the method code will ever execute.
 */
fun MutableMethod.returnEarly() {
    check(returnType.first() == 'V') {
        RETURN_TYPE_MISMATCH
    }
    addInstructions(0, "return-void")
}

/**
 * Overrides the first instruction of a method with a constant `Boolean` return value.
 * None of the original method code will execute.
 */
fun MutableMethod.returnEarly(value: Boolean) {
    check(returnType.first() == 'Z') {
        RETURN_TYPE_MISMATCH
    }
    val overrideValue = if (value) "0x1" else "0x0"
    addInstructions(
        0,
        """
            const/4 v0, $overrideValue
            return v0
        """
    )
}

/**
 * Overrides the first instruction of a method with a constant `Int` return value.
 * None of the original method code will execute.
 */
fun MutableMethod.returnEarly(value: Int) {
    check(returnType.first() == 'I') { RETURN_TYPE_MISMATCH }
    addInstructions(
        0,
        """
            const v0, $value
            return v0
        """
    )
}

/**
 * Overrides the first instruction of a method with a constant `String` return value.
 * None of the original method code will execute.
 */
fun MutableMethod.returnEarly(value: String) {
    check(returnType == "Ljava/lang/String;") { RETURN_TYPE_MISMATCH }
    addInstructions(
        0,
        """
            const-string v0, "$value"
            return-object v0
        """
    )
}

/**
 * @return An immutable list of indices of the instructions in reverse order.
 */
fun MutableMethod.findInstructionIndicesReversed(filter: Instruction.() -> Boolean): List<Int> = instructions
    .withIndex()
    .filter { (_, instruction) -> filter(instruction) }
    .map { (index, _) -> index }
    .asReversed()

/**
 * @return An immutable list of indices of the instructions in reverse order.
 * @throws IllegalArgumentException if no matching indices are found.
 */
fun MutableMethod.findInstructionIndicesReversedOrThrow(filter: Instruction.() -> Boolean): List<Int> {
    val indexes = findInstructionIndicesReversed(filter)
    if (indexes.isEmpty()) throw IllegalArgumentException("No matching instructions found in: $this")
    return indexes
}
