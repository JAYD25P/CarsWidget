# MOTORVAULT (CarsWidget)

Independent Android automotive art collection and home-screen widget app. PokéWidget is the architectural reference.

## UI
Motorsport-inspired graphite surfaces and racing-orange accents; featured vehicle, searchable collection, category/style filters, Discover / Widget Studio / Asset Library navigation; custom vector adaptive launcher icon.

## Vehicle sources and licences
- OpenGameArt Sports Cars Pixelart Sprites (32×32 sprites), CC0: https://opengameart.org/content/sports-cars-pixelart-sprites
- OpenGameArt Top-down Sports Car Sprite, CC0: https://opengameart.org/content/top-down-sports-car-sprite
- Spectral GT RS rendered views and game-ready GLB, CC0 1.0: https://github.com/JaronKBragg7337/spectral-gt-rs
- Khronos Car Concept GLB, CC-BY-4.0: credit Darmstadt Graphics Group / Eric Chadwick (based on a CC0 model by Unity Fan).
- Khronos Toy Car GLB, CC0-1.0: 3dimentional.
- Realistic Racing Mega Pack asset manifest and credits: https://github.com/pocketagent98-ai/realistic-racing-pack

Gradle downloads the source PNGs and GLB files into generated Android resources/assets. GLBs are bundled for future in-app 3D viewing; widgets use raster artwork because Android RemoteViews cannot display live GLB scenes.

## Build
GitHub Actions → Android debug APK → Run workflow. Download the autowidget-debug artifact.

## Widget constraint
Launcher widgets use RemoteViews and cannot host a continuously running custom animation surface. Rotation is implemented as scheduled image changes and may be deferred by Android power management or the launcher.
