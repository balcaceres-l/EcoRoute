# Implementation Plan - Fix Build Errors

The project is currently failing to build due to an invalid attribute in `activity_main.xml`. Specifically, `app:navigationBarIndicatorColor` is not a valid attribute for `BottomNavigationView`. In Material 3, the correct attribute to set the active indicator color directly on the component is `app:itemActiveIndicatorColor`.

## User Review Required

> [!IMPORTANT]
> The fix involves changing the attribute name in the layout file. This is a straightforward fix for a common Material 3 integration error.

## Proposed Changes

### [app]

#### [MODIFY] [activity_main.xml](file:///home/marioalabi/Nextcloud/Unicaes_Ciclo_8/DESARROLLO%20DE%20APLICACIONES%20MOVILES%20AVANZADAS/Periodo%201/EcoRoute/app/src/main/res/layout/activity_main.xml)
- Change `app:navigationBarIndicatorColor` to `app:itemActiveIndicatorColor`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the project builds successfully.

### Manual Verification
- None required as this is a build-time fix.
