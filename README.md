# 👋🧩 Morphe Patches template

Template repository for Morphe Patches.

## ❓ About

Patches for apps I like.

<!-- TODO: Update this about section with a brief introduction/summary about this repo and what it offers. -->

### How to use these patches

Click the button below, or manually add this repo url to Morphe Manager: https://github.com/xyz-user/xyz-patches

<a href="https://morphe.software/add-source?github=xyz-user/xyz-patches">
  <img src=".github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48">
</a>

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->

<!-- Do not modify this section by hand. The patch list is generated when release.yml creates a new release.
     
     If you wish for the patches list to be collapsed, then remove the word 'EXPANDED' from the comment tag above.

     If you wish to manually keep this list updated then remove the PATCHES_START and PATCHES_END 
     comment blocks entirely. -->

#### A list of your patches will automatically be shown here after your first patches release is created.

&nbsp;

## 🚀 Getting development started

To start using this template, follow these steps:

1. [Setup](https://github.com/MorpheApp/morphe-documentation/blob/main/docs/morphe-development/README.md) your development environment including adding a GitHub PAT as described [here](https://github.com/MorpheApp/morphe-patcher/blob/main/docs/2_1_setup.md#-prepare-the-environment).
2. [Create a new repository using this template](https://github.com/new?template_name=morphe-patches-template&template_owner=MorpheApp). Select create a new repository, and **enable 'Include all branches'** 
3. Enable "Allow GitHub Actions to create and approve pull requests" in your repo Settings > Actions > General > Workflow permissions
4. Update the [build.gradle.kts](patches/build.gradle.kts) file (Specifically, the 
   [group of the project](patches/build.gradle.kts#L1), and the [About](patches/build.gradle.kts#L6-L11))
5. Update the [README.md](README.md) file to be specific of your repo, and update the links in the [issue templates](.github/ISSUE_TEMPLATE).
6. Choose a name for your patches project. Keep in mind you must use a name that does not 
   imply authorship by the Morphe open source project. If unsure, then simply name these
   patches after yourself ("UserXYZ Morphe patches"). See the [NOTICE](NOTICE) for details. 
7. (Optional): Add `patches-bundle.png` to the project if you want a custom icon to show in
   Morphe Manager instead of your GitHub profile avatar.
8. (Optional): Sign your releases, so Morphe Manager refuses a release that was changed or swapped after you published it:
   1. Run `./gradlew generateSigningKey`. It creates `morphe-signing.pub` and `morphe-signing.key`.
   2. Commit `morphe-signing.pub`. Never commit `morphe-signing.key`, it is already ignored by git.
   3. Add the content of `morphe-signing.key` as the `MORPHE_SIGNING_KEY` secret in your repo
      Settings > Secrets and variables > Actions, then keep a backup of it somewhere safe and delete the file.

   Every release is signed from then on. If you lose the key or it leaks, create a new one the same way;
   users then have to confirm the new key in Morphe Manager before they get your next release.

🎉 You are now ready to start creating patches!

## 🧑‍💻 Dev usage

To develop and release your Patches using this template:

- **Make all changes to the `dev` branch.**
- For local development work build your patches using the gradle task `./gradlew buildAndroid` to generate the mpp file found in `patches/build/libs/patches-*.mpp`. Apply your patches locally using Morphe Desktop tool like any other patch bundle.
- Always use [Semantic commit](https://kapeli.com/cheat_sheets/Semantic_Commits.docset/Contents/Resources/Documents/index) messages for commits. To keep it simple use only 3 commit message types: 
  - `feat: Added a new feature`
  - `fix: Some problem now fixed`
  - `chore: Random change you do not want in the user facing changelog`
- Commits of `fix:` and `feat:` will automatically generate new pre-releases and `chore:` will not create a new release.
- Users can apply your dev branch releases by enabling `pre-release` in Morphe Manager patch sources.
- When your dev branch is ready, and you want a stable release, merge dev branch to main (do not squash, and only merge).
- **Always use semantic release (release.yml)**. Do not manually upload or create releases by hand
  because many files must be updated and release.yml handles everything.

## 🤓 Tips
- See the [patcher documentation](https://github.com/MorpheApp/morphe-patcher/blob/main/docs/1_patcher_intro.md) for more examples of creating patches and fingerprints.
- Do not use AI to create new release scripts. The `release.yml` here already handles everything.
  If you need something custom with your releases then modify the existing `release.yml`
  and `.releaserc` instead of writing everything new from scratch.
- Do not manually edit or manually commit any generated files such as: `patches-list.json`,
  `patches-bundle.json`, `CHANGELOG.md`.  These files will be automatically updated by `release.yml`.
- Do not force push any semantic release commits as that will break all future releases.
  If you need to fix a broken release, it's always easiest to create a new release instead of 
  fixing an existing release.


<!-- The patches end tag is intentionally placed here so the first release will clean up 
     this readme of all developer instructions above. -->
<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## 📜 License

UserXYZ Patches are licensed under the [GNU General Public License v3.0](LICENSE)
