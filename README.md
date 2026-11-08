# AutoWidget

An Android automotive-art collection and home-screen widget application. This repository is independent of PokéWidget and uses its widget-first architecture as a technical reference.

## Current prototype
- Automotive collection with performance, SUV, classic, passenger, utility, two-wheel, commercial, motorsport and electric categories.
- Selectable car artwork and a Compose collection browser.
- Android home-screen widget with transparent presentation.
- Static artwork and an experimental low-frequency rotating collection mode.
- GitHub Actions debug APK build.

## Build
GitHub Actions → **Android debug APK** → Run workflow. Download the `autowidget-debug` artifact.

## Artwork
The initial illustrations are original vector placeholders. The catalog and renderer are designed to accept imported PNG, WebP, GIF, APNG and frame-sequence artwork in later iterations.

## Animation note
Android launcher widgets use RemoteViews and do not support a continuously running custom animation surface. The prototype uses scheduled frame changes, which launchers and Android power management may defer. It is intentionally not a 60-FPS animation system.
