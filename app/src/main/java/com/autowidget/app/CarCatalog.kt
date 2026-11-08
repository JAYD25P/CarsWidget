package com.autowidget.app

data class CarModel(val id:String,val name:String,val category:String,val art:Int,val style:String,val subtitle:String)
object CarCatalog {
 val cars=listOf(
  CarModel("spectral","Spectral GT RS","Performance",R.drawable.spectral_gt_beauty,"Realistic 3D","CC0 · 4 animation clips"),
  CarModel("concept","Car Concept","Performance",R.drawable.car_concept_preview,"Realistic 3D","Khronos · CC-BY 4.0"),
  CarModel("chev","Sports Car 01","Performance",R.drawable.car_chev,"Pixel art","OpenGameArt · CC0"),
  CarModel("f1pixel","Formula Racer","Motorsport",R.drawable.car_f1pixel,"Pixel art","OpenGameArt · CC0"),
  CarModel("pointy","Sports Car 02","Performance",R.drawable.car_pointy,"Pixel art","OpenGameArt · CC0"),
  CarModel("sportpixel","Sports Car 03","Performance",R.drawable.car_sportpixel,"Pixel art","OpenGameArt · CC0"),
  CarModel("topdown","Top-down Racer","Motorsport",R.drawable.car_topdown,"Pixel art","OpenGameArt · CC0"),
  CarModel("toy","Toy Car","Collection",R.drawable.toy_car_preview,"3D model","Khronos · CC0")
 )
 fun byId(id:String)=cars.firstOrNull{it.id==id}?:cars.first()
}