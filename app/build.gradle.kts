plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.autowidget.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.autowidget.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

val downloadedVehicleRes = layout.buildDirectory.dir("generated/vehicle-art/res")
android.sourceSets.getByName("main").res.srcDir(downloadedVehicleRes)

val downloadVehicleArtwork by tasks.registering {
    outputs.dir(downloadedVehicleRes)
    doLast {
        val drawableDir = downloadedVehicleRes.get().dir("drawable-nodpi").asFile
        drawableDir.mkdirs()
        val assets = mapOf(
            "car_chev.png" to "https://opengameart.org/sites/default/files/chev.png",
            "car_f1pixel.png" to "https://opengameart.org/sites/default/files/f1car.png",
            "car_pointy.png" to "https://opengameart.org/sites/default/files/pointy.png",
            "car_sportpixel.png" to "https://opengameart.org/sites/default/files/sportscar1.png",
            "car_topdown.png" to "https://opengameart.org/sites/default/files/sports-car_0.png"
        )
        assets.forEach { (fileName, source) ->
            val target = drawableDir.resolve(fileName)
            if (!target.exists() || target.length() == 0L) {
                logger.lifecycle("Downloading CC0 vehicle artwork: $fileName")
                val connection = java.net.URL(source).openConnection()
                connection.setRequestProperty("User-Agent", "CarsWidget-AndroidBuild/1.1")
                connection.getInputStream().use { input -> target.outputStream().use { output -> input.copyTo(output) } }
                require(target.length() > 0L) { "Downloaded asset is empty: $fileName" }
            }
        }
    }
}
tasks.named("preBuild").configure { dependsOn(downloadVehicleArtwork) }

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.core:core-ktx:1.15.0")
}
