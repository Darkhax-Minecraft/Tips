# [Tips](https://www.curseforge.com/minecraft/mc-mods/tips)

This mod displays useful tips on Minecraft's various loading screens. The tip displayed will be cycled out every 5
seconds. New tips can be added or removed by other mods and modpacks very easily.

![Screenshot](https://i.imgur.com/bHolrsC.png)

## For contributors

Propose new tips and translations by:

- [creating an issue using Tip or Translate template](https://github.com/Darkhax-Minecraft/Tips/issues/new/choose)
  (the ***leave it to others*** way)
- **OR** creating a pull request (the ***do-it-yourself*** way)
    1. fork a repository
    2. add new entry file in
       the [`Common/src/main/resources/assets/tipsmod/tips/`](Common/src/main/resources/assets/tipsmod/tips/) directory
    3. add translation in respective files in
       the [`Common/src/main/resources/assets/tipsmod/lang/`](Common/src/main/resources/assets/tipsmod/lang/) directory
    4. submit a pull request

## For developers

### Maven Dependency

If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle`
file. Make sure to replace the version with the correct one. All versions can be
viewed [here](https://maven.blamejared.com/net/darkhax/openloader/).

```gradle
repositories {

    maven { url 'https://maven.blamejared.com' }
}

dependencies {

    // Example: compile "net.darkhax.tips:Tips-1.16.4:3.0.3"
    compile "net.darkhax.tips:Tips-MCVERSION:PUT_VERSION_HERE"
}
```

### Jar Signing

As of January 11th 2021 officially published builds will be signed. You can validate the integrity of these builds by
comparing their signatures with the public fingerprints.

| Hash   | Fingerprint                                                        |
|--------|--------------------------------------------------------------------|
| MD5    | `12F89108EF8DCC223D6723275E87208F`                                 |
| SHA1   | `46D93AD2DC8ADED38A606D3C36A80CB33EFA69D1`                         |
| SHA256 | `EBC4B1678BF90CDBDC4F01B18E6164394C10850BA6C4C748F0FA95F2CB083AE5` |

## Sponsors

[//]: # (https://github.blog/changelog/2022-05-19-specify-theme-context-for-images-in-markdown-beta/)
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://cms-assets.nodecraft.com/f/133932/1938x400/63d810269f/logo-on-black-download.png">
  <img src="https://cms-assets.nodecraft.com/f/133932/1938x400/9e01cbcb2d/logo-on-white-download.png">
</picture>

This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month
of service!