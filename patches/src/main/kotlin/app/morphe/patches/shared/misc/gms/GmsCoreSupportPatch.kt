package app.morphe.patches.shared.misc.gms

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchBuilder
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.Patch
import app.morphe.patcher.patch.ResourcePatchBuilder
import app.morphe.patcher.patch.ResourcePatchContext
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.all.misc.packagename.changePackageNamePatch
import app.morphe.patches.all.misc.packagename.setOrGetFallbackPackageName
import app.morphe.patches.shared.misc.gms.Constants.ACTIONS
import app.morphe.patches.shared.misc.gms.Constants.AUTHORITIES
import app.morphe.patches.shared.misc.gms.Constants.PERMISSIONS
import app.morphe.util.getNode
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import org.w3c.dom.Element
import org.w3c.dom.Node

/**
 * A patch that allows patched Google apps to run without root and under a different package name
 * by using GmsCore instead of Google Play Services.
 */
@Suppress("UNUSED_PARAMETER")
fun gmsCoreSupportPatch(
    fromPackageName: String,
    toPackageName: String,
    primeMethodFingerprint: Fingerprint? = null,
    earlyReturnFingerprints: Set<Fingerprint> = setOf(),
    mainActivityOnCreateFingerprint: Fingerprint,
    extensionPatch: Patch<*>,
    gmsCoreSupportResourcePatchFactory: () -> Patch<*>,
    executeBlock: BytecodePatchContext.() -> Unit = {},
    block: BytecodePatchBuilder.() -> Unit = {},
) = bytecodePatch(
    name = "GmsCore support",
    description = "Allows the app to work without root by using a different package name.",
) {
    dependsOn(
        changePackageNamePatch,
        gmsCoreSupportResourcePatchFactory(),
        extensionPatch,
    )

    execute {
        val packageName = setOrGetFallbackPackageName(toPackageName)

        // Return these methods early to prevent the app from crashing.
        earlyReturnFingerprints.forEach {
            it.method.apply {
                if (returnType == "Z") {
                    returnEarly(false)
                } else {
                    returnEarly()
                }
            }
        }
        ServiceCheckFingerprint.method.returnEarly()

        // Google Play Utility is not present in all apps.
        if (GooglePlayUtilityFingerprint.methodOrNull != null) {
            GooglePlayUtilityFingerprint.method.returnEarly(0)
        }

        // Set original package name for extension.
        OriginalPackageNameExtensionFingerprint.method.returnEarly(fromPackageName)

        // Add GmsCore check to main activity.
        mainActivityOnCreateFingerprint.method.addInstruction(
            0,
            "invoke-static/range { p0 .. p0 }, $EXTENSION_CLASS_DESCRIPTOR->" +
                    "checkGmsCore(Landroid/app/Activity;)V"
        )

        // Change the vendor of GmsCore in the extension.
        GmsCoreSupportFingerprint.method.returnEarly(GMS_CORE_VENDOR_GROUP_ID)

        executeBlock()
    }

    block()
}

/**
 * Abstract resource patch for GmsCore support.
 */
@Suppress("UNUSED_PARAMETER")
fun gmsCoreSupportResourcePatch(
    fromPackageName: String,
    toPackageName: String,
    spoofedPackageSignature: String,
    executeBlock: ResourcePatchContext.() -> Unit = {},
    block: ResourcePatchBuilder.() -> Unit = {},
) = resourcePatch {
    dependsOn(changePackageNamePatch)

    execute {
        fun Node.adoptChild(
            tagName: String,
            block: Element.() -> Unit,
        ) {
            val child = ownerDocument.createElement(tagName)
            child.block()
            appendChild(child)
        }

        document("AndroidManifest.xml").use { document ->
            val applicationNode = document.getElementsByTagName("application").item(0)

            // Spoof package name and signature.
            applicationNode.adoptChild("meta-data") {
                setAttribute("android:name", "$GMS_CORE_VENDOR_GROUP_ID.android.gms.SPOOFED_PACKAGE_NAME")
                setAttribute("android:value", fromPackageName)
            }
            applicationNode.adoptChild("meta-data") {
                setAttribute("android:name", "$GMS_CORE_VENDOR_GROUP_ID.android.gms.SPOOFED_PACKAGE_SIGNATURE")
                setAttribute("android:value", spoofedPackageSignature)
            }
        }

        executeBlock()
    }

    block()
}
