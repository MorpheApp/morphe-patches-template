package app.morphe.patches.googlephotos.misc.features

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringsOption
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

val spoofFeaturesPatch = bytecodePatch(
    name = "Spoof features",
    description = "Spoofs the device to enable Google Pixel exclusive features, including unlimited storage.",
) {
    compatibleWith("com.google.android.apps.photos")

    dependsOn(spoofBuildInfoPatch)

    val featuresToEnable by stringsOption(
        key = "featuresToEnable",
        default = listOf(
            "com.google.android.apps.photos.NEXUS_PRELOAD",
            "com.google.android.apps.photos.nexus_preload",
        ),
        title = "Features to enable",
        description = "Google Pixel exclusive features to enable.",
        required = true,
    )

    val featuresToDisable by stringsOption(
        key = "featuresToDisable",
        default = listOf(
            "com.google.android.apps.photos.PIXEL_2017_PRELOAD",
            "com.google.android.apps.photos.PIXEL_2018_PRELOAD",
        ),
        title = "Features to disable",
        description = "Google Pixel exclusive features to disable.",
        required = true,
    )

    execute {
        @Suppress("NAME_SHADOWING")
        val featuresToEnable = featuresToEnable!!.toSet()

        @Suppress("NAME_SHADOWING")
        val featuresToDisable = featuresToDisable!!.toSet()

        initializeFeaturesEnumFingerprint.method.apply {
            instructions.filter { it.opcode == Opcode.CONST_STRING }.forEach {
                val feature = it.getReference<StringReference>()!!.string

                val spoofedFeature = when (feature) {
                    in featuresToEnable -> "android.hardware.wifi"
                    in featuresToDisable -> "dummy"
                    else -> return@forEach
                }

                val constStringIndex = it.location.index
                val constStringRegister = (it as OneRegisterInstruction).registerA

                replaceInstruction(
                    constStringIndex,
                    "const-string v$constStringRegister, \"$spoofedFeature\"",
                )
            }
        }
    }
}
