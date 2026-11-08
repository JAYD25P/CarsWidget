# CarsWidget

A dedicated Android automotive collection and home-screen widget app. PokéWidget is used as an architectural reference; this repository is independent.

## Current asset integration

The prototype's hand-authored vector vehicle placeholders have been removed from the catalog. The Android build now downloads and packages these five CC0 PNG sprites from OpenGameArt:

| Asset | Source | Licence |
|---|---|---|
| Sports cars pixelart sprites — chev.png | https://opengameart.org/content/sports-cars-pixelart-sprites | CC0 |
| Sports cars pixelart sprites — f1car.png | https://opengameart.org/content/sports-cars-pixelart-sprites | CC0 |
| Sports cars pixelart sprites — pointy.png | https://opengameart.org/content/sports-cars-pixelart-sprites | CC0 |
| Sports cars pixelart sprites — sportscar1.png | https://opengameart.org/content/sports-cars-pixelart-sprites | CC0 |
| Top-down sports car — sports-car.png | https://opengameart.org/content/top-down-sports-car-sprite | CC0 |

Assets are downloaded at build time into generated Android drawable resources; source artwork is not modified. The OpenGameArt pages identify the first set as 32×32 pixel-art sprites and both works as CC0.

## Planned 3D and realistic artwork sources

- Poly Pizza Sports Car (Quaternius): https://poly.pizza/m/1mkmFkAz5v — GLTF/OBJ, CC0.
- Poly Pizza Cars Bundle: https://poly.pizza/bundle/Cars-Bundle-FE5IWe6OMk — eight models, GLB/FBX, CC0.
- Poly Pizza search: https://poly.pizza/search/automobile
- Itch.io racing asset listings: https://itch.io/game-assets/tag-racing

GLB models are not Android RemoteViews images. They require a 3D viewer in the app and pre-rendered PNG/WebP snapshots for launcher widgets. The Spectral GT RS model and a specific Realistic Racing Mega Pack listing still need their exact asset-page URLs before they can be safely pinned to the app's asset manifest.

## Build

GitHub Actions → **Android debug APK** → Run workflow. Download the `autowidget-debug` artifact.

## Widget constraints

Android launcher widgets use RemoteViews and cannot host a continuously running custom animation surface. Rotation is implemented as scheduled image changes and may be deferred by Android power management or the launcher.
