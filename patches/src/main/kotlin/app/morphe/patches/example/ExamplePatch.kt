package app.morphe.patches.example

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/ExamplePatch;"
private const val ORIGINAL_CLASS_NAME = "Lzbiletem/zbiletem/model/ticket/various/VariousTicket;"

@Suppress("unused")
val examplePatch = bytecodePatch(
    name = "Example Patch",
    description = "Example patch to start with."
) {
    compatibleWith("zbiletem.zbiletem"("4.8.3"))

    extendWith("extensions/extension.rve")

    // Business logic of the patch to disable ads in the app.
    execute {
        VariousTicketFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual {p0}, ${ORIGINAL_CLASS_NAME}->getValidFromDateTime()Lorg/joda/time/DateTime;
                iget-object v0, p0, ${ORIGINAL_CLASS_NAME}->validFrom:Ljava/lang/String;
                return-object v0
            """
        )
    }
}
