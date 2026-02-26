package app.morphe.patches.all.misc.packagename

import app.morphe.patcher.patch.Option
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.util.asSequence
import app.morphe.util.getNode
import org.w3c.dom.Element

lateinit var packageNameOption: Option<String>

fun setOrGetFallbackPackageName(fallbackPackageName: String): String {
    val packageName = packageNameOption.value!!
    return if (packageName == packageNameOption.default) {
        fallbackPackageName.also { packageNameOption.value = it }
    } else {
        packageName
    }
}

val changePackageNamePatch = resourcePatch(
    name = "Change package name",
    description = "Appends \".morphe\" to the package name by default.",
    use = false,
) {
    packageNameOption = stringOption(
        key = "packageName",
        default = "Default",
        values = mapOf("Default" to "Default"),
        title = "Package name",
        description = "The name of the package to rename the app to.",
        required = true,
    ) {
        it == "Default" || it!!.matches(Regex("^[a-z]\\w*(\\.[a-z]\\w*)+\\$"))
    }

    finalize {
        document("AndroidManifest.xml").use { document ->
            val manifest = document.getNode("manifest") as Element
            val packageName = manifest.getAttribute("package")
            val newPackageName = "$packageName.morphe"
            manifest.setAttribute("package", newPackageName)
        }
    }
}
