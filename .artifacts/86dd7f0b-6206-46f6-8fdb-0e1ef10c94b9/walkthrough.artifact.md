# Walkthrough - Build Error Fixed

I have fixed the build error in `activity_main.xml` by removing an invalid/redundant attribute.

## Changes Made

### [app]

#### [MODIFY] [activity_main.xml](file:///home/marioalabi/Nextcloud/Unicaes_Ciclo_8/DESARROLLO%20DE%20APLICACIONES%20MOVILES%20AVANZADAS/Periodo%201/EcoRoute/app/src/main/res/layout/activity_main.xml)
- Removed `app:navigationBarIndicatorColor` (and the attempted `app:itemActiveIndicatorColor`).

## Why this works
The attribute `app:navigationBarIndicatorColor` does not exist in the Material Components library. While `app:itemActiveIndicatorColor` is the standard Material 3 attribute for this, it was also failing to resolve in this project's current configuration.

However, since your theme ([themes.xml](file:///home/marioalabi/Nextcloud/Unicaes_Ciclo_8/DESARROLLO%20DE%20APLICACIONES%20MOVILES%20AVANZADAS/Periodo%201/EcoRoute/app/src/main/res/values/themes.xml)) already defines `colorSecondaryContainer`, the `BottomNavigationView` will automatically use that color for the active indicator. Explicitly setting it in the layout was redundant and causing the build to fail.

## Verification Results

### Automated Tests
- Ran `./gradlew :app:assembleDebug` and the build finished successfully.
