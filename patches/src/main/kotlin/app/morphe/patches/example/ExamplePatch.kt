package app.morphe.patches.example

import app.morphe.patcher.Fingerprint
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

    extendWith("extensions/extension.mpe")

    // Business logic of the patch to disable ads in the app.
    execute {
        val fingers = listOf<String>("getActiveTill").forEach {a ->
            Fingerprint(custom = {
                method, classDef -> method.name == a && classDef.type == ORIGINAL_CLASS_NAME
            })
        }

        VariousTicketFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual {p0}, ${ORIGINAL_CLASS_NAME}->getValidFromDateTime()Lorg/joda/time/DateTime;
                iget-object v0, p0, ${ORIGINAL_CLASS_NAME}->validFrom:Ljava/lang/String;
                return-object v0
            """
        )
        TestFingerprint.match().method.addInstructions(
            0,
            """
                const-string v0, "test123"
                return-object v0
            """
        )
    }
}
