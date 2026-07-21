# Implementation Plan - Fix Build Errors in MapFragment

The build is failing because `findViewById` calls in `MapFragment.kt` cannot infer their type, which also causes `setOnClickListener` to be unresolved. I will add explicit type parameters to these calls.

## Proposed Changes

### [app]

#### [MODIFY] [MapFragment.kt](file:///home/marioalabi/Nextcloud/Unicaes_Ciclo_8/DESARROLLO%20DE%20APLICACIONES%20MOVILES%20AVANZADAS/Periodo%201/EcoRoute/app/src/main/java/com/example/ecoroute/MapFragment.kt)
- Add type parameters to `findViewById` for `btnZoomIn`, `btnZoomOut`, and `btnMyLocation`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the fix.
