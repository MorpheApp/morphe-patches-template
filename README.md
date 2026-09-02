# 🧩 RxZry Morphe Patches

Custom patches for Morphe to enhance Android applications, remove ads, and improve user experience.

---

## ❓ About

This repository provides custom bytecode patches for selected Android applications using the **Morphe Patcher** framework.

### 📱 Supported Apps & Features

| App | Package Name | Patch | Description |
| :--- | :--- | :--- | :--- |
| **Kickbase** | `com.kkstr.bundesliga` | `Disable Kickbase Ads` | Blocks in-app banner ads, full-screen ads, and ad placement auto-reloading. |
| **Skat** | `com.isarinteractive.skat.android.ad` | `Disable Ads` | Disables banner and fullscreen ads and signals ad completion directly to the game engine. |

---

## 📥 How to Use in Morphe Manager

1. Open **Morphe Manager** on your Android device.
2. Go to **Settings** ⚙️ > **Sources** > **Patches Source**.
3. Set the source to:
   ```text
   RxZry/morphe-patches
   ```
4. Select the target APK/APKM from your storage and apply the patches!

---

## 🩹 Patches List

<!-- PATCHES_START EXPANDED -->

- **Disable Kickbase Ads** (`com.kkstr.bundesliga`)
  - Disables banner ads, full-screen ads, and in-feed ads in Kickbase.
- **Disable Ads** (`com.isarinteractive.skat.android.ad`)
  - Disables banner and fullscreen ads, and automatically signals ad completion to the game engine.

<!-- PATCHES_END -->

---

## 🛠️ Building Locally

To build the `.mpp` patch bundle locally:

```bash
# Build MPP patch bundle and generate patch list
./gradlew :patches:build generatePatchesList -x :extensions:extension:compileReleaseJavaWithJavac -x :extensions:extension:assembleRelease
```

The compiled patch bundle will be located at:
`patches/build/libs/patches-*.mpp`

---

## 🤖 AI Disclaimer

> [!NOTE]
> Parts of the reverse engineering analysis, fingerprint definitions, and documentation in this repository were created or assisted by Artificial Intelligence (AI) paired with human verification. While all patches are tested and validated for functionality, they are provided "as-is" without warranty of any kind. Use at your own discretion.

---

## 📜 License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
