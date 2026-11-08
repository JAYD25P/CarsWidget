plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.android")
 id("org.jetbrains.kotlin.plugin.compose")
}
android {
 namespace="com.autowidget.app"; compileSdk=35
 defaultConfig { applicationId="com.autowidget.app"; minSdk=26; targetSdk=35; versionCode=3; versionName="1.2" }
 buildFeatures { compose=true }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
}
val genRes=layout.buildDirectory.dir("generated/vehicle-art/res")
val genAssets=layout.buildDirectory.dir("generated/vehicle-art/assets")
android.sourceSets.getByName("main").res.srcDir(genRes)
android.sourceSets.getByName("main").assets.srcDir(genAssets)
val fetchVehicleLibrary by tasks.registering {
 outputs.dir(genRes); outputs.dir(genAssets)
 doLast {
  val drawable=genRes.get().dir("drawable-nodpi").asFile.apply{mkdirs()}
  val models=genAssets.get().dir("vehicles").asFile.apply{mkdirs()}
  val images=mapOf(
   "car_chev.png" to "https://opengameart.org/sites/default/files/chev.png",
   "car_f1pixel.png" to "https://opengameart.org/sites/default/files/f1car.png",
   "car_pointy.png" to "https://opengameart.org/sites/default/files/pointy.png",
   "car_sportpixel.png" to "https://opengameart.org/sites/default/files/sportscar1.png",
   "car_topdown.png" to "https://opengameart.org/sites/default/files/sports-car_0.png",
   "spectral_gt_beauty.png" to "https://raw.githubusercontent.com/JaronKBragg7337/spectral-gt-rs/main/spectral_gt_beauty.png",
   "spectral_gt_night.png" to "https://raw.githubusercontent.com/JaronKBragg7337/spectral-gt-rs/main/spectral_gt_night.png",
   "spectral_gt_profile.png" to "https://raw.githubusercontent.com/JaronKBragg7337/spectral-gt-rs/main/spectral_gt_profile.png"
  )
  images.forEach{(name,url)-> val f=drawable.resolve(name); if(!f.exists()||f.length()==0L){ val c=java.net.URL(url).openConnection(); c.connectTimeout=30000;c.readTimeout=60000;c.setRequestProperty("User-Agent","MOTORVAULT/1.2");c.getInputStream().use{i->f.outputStream().use{o->i.copyTo(o)}};require(f.length()>0){"Empty image $name"} } }
  val glbs=mapOf(
   "spectral_gt_rs_game_ready.glb" to "https://raw.githubusercontent.com/JaronKBragg7337/spectral-gt-rs/main/spectral_gt_rs_game_ready.glb",
   "car_concept.glb" to "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Assets/main/Models/CarConcept/glTF-Binary/CarConcept.glb",
   "toy_car.glb" to "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Assets/main/Models/ToyCar/glTF-Binary/ToyCar.glb"
  )
  glbs.forEach{(name,url)->val f=models.resolve(name);if(!f.exists()||f.length()==0L){val c=java.net.URL(url).openConnection();c.connectTimeout=30000;c.readTimeout=120000;c.setRequestProperty("User-Agent","MOTORVAULT/1.2");c.getInputStream().use{i->f.outputStream().use{o->i.copyTo(o)}};require(f.length()>1000L){"Invalid GLB $name"}}}
 }
}
tasks.named("preBuild").configure{dependsOn(fetchVehicleLibrary)}
dependencies {
 implementation(platform("androidx.compose:compose-bom:2024.12.01"))
 implementation("androidx.activity:activity-compose:1.10.0")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.material:material-icons-extended")
 implementation("androidx.core:core-ktx:1.15.0")
}