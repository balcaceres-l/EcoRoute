# Walkthrough - MapFragment Build Fix

I have fixed the build errors in `MapFragment.kt` related to view type inference.

## Changes Made

### [app]

#### [MODIFY] [MapFragment.kt](file:///home/marioalabi/Nextcloud/Unicaes_Ciclo_8/DESARROLLO%20DE%20APLICACIONES%20MOVILES%20AVANZADAS/Periodo%201/EcoRoute/app/src/main/java/com/example/ecoroute/MapFragment.kt)
- Added missing imports: `ImageButton`, `MaterialButton`, and `FloatingActionButton`.
- Added explicit type parameters to `findViewById` calls to allow the compiler to correctly resolve `setOnClickListener`.

## Verification Results

### Automated Tests
- Ran `./gradlew :app:assembleDebug` and the build finished successfully.
