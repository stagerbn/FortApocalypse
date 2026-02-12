# Fort Apocalypse Echo (Android)

Eine kleine, moderne Android-Gaming-App in Jetpack Compose, inspiriert von **Fort Apocalypse**:

- Du steuerst einen Helikopter per Drag-Gesten.
- Du rettest Überlebende auf der Karte.
- Treibstoff sinkt ständig – effiziente Bewegung ist wichtig.
- Nach dem Retten aller Überlebenden steigst du ins nächste Level auf.

## Features

- Kotlin + Jetpack Compose
- Einfache Arcade-Game-Loop mit `LaunchedEffect`
- HUD mit Score, Level und Fuel
- Neustart-Dialog bei Missionsfehlschlag

## Schnellstart unter Windows (empfohlen: Android Studio)

### 1) Voraussetzungen installieren

- **Android Studio** (aktuelle Stable-Version)
- **Android SDK Platform 34** + Build-Tools (über SDK Manager)
- **Android Emulator** oder ein echtes Android-Gerät
- **JDK 17** (in Android Studio unter `File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JDK`)

> Wichtig: Das Projekt ist auf Java/Kotlin JVM **17** konfiguriert.

### 2) Projekt öffnen

1. Android Studio starten.
2. `File -> Open` und den Ordner `FortApocalypse` auswählen.
3. Warten, bis **Gradle Sync** durchgelaufen ist.
4. Falls Android Studio nach Komponenten fragt: installieren/übernehmen.

### 3) Emulator einrichten (falls noch keiner vorhanden)

1. `Tools -> Device Manager`
2. `Create Device`
3. Ein Profil (z. B. Pixel 6) auswählen
4. Ein System-Image (Android 14 / API 34) laden
5. Emulator starten

### 4) App starten

1. Run-Konfiguration `app` auswählen
2. Zielgerät (Emulator oder physisches Gerät) wählen
3. Auf **Run (▶)** klicken

Die App startet direkt in `MainActivity` und ist als Launcher-Activity gesetzt.

---

## Alternativ per Terminal unter Windows

Wenn Gradle installiert ist (oder via Gradle Wrapper, sobald vorhanden):

```powershell
# im Projektordner
./gradlew.bat assembleDebug
```

APK-Pfad nach erfolgreichem Build:

```text
app\build\outputs\apk\debug\app-debug.apk
```

Installation auf angeschlossenes Gerät (mit aktivem USB-Debugging):

```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

## Häufige Probleme unter Windows

- **"Gradle JDK ist falsch" / Java 21+ Probleme**  
  -> In Android Studio explizit **JDK 17** auswählen.

- **Sync-Fehler bei Dependencies**  
  -> Firewall/Proxy prüfen, Zugriff auf `dl.google.com` und `repo.maven.apache.org` erlauben.

- **Emulator ist langsam**  
  -> BIOS/UEFI-Virtualisierung aktivieren (Intel VT-x / AMD-V), Hypervisor korrekt einrichten.

## Projektstruktur (kurz)

- `app/src/main/java/com/example/fortapocalypse/MainActivity.kt` – Spiel-Logik + UI
- `app/src/main/AndroidManifest.xml` – App/Launcher-Konfiguration
- `app/build.gradle.kts` – Android-, Kotlin- und Compose-Konfiguration

## Hinweis zur CI-/Container-Umgebung hier

In dieser Umgebung sind Maven/Google Repository Downloads gesperrt (HTTP 403), daher konnte hier kein vollständiger Build durchgeführt werden. Lokal auf Windows mit normalem Internetzugriff sollte es funktionieren.
