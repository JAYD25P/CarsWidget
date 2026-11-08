package com.autowidget.app

data class CarModel(val id:String,val name:String,val category:String,val art:Int,val style:String="Pixel art")

object CarCatalog {
    val cars = listOf(
        CarModel("chev","Chevrolet-style Sports Car","Performance",R.drawable.car_chev),
        CarModel("f1pixel","Formula Racer","Motorsport",R.drawable.car_f1pixel),
        CarModel("pointy","Point-Nose Sports Car","Performance",R.drawable.car_pointy),
        CarModel("sportpixel","Compact Sports Car","Performance",R.drawable.car_sportpixel),
        CarModel("topdown","Top-down Sports Car","Performance",R.drawable.car_topdown)
    )
    fun byId(id:String)=cars.firstOrNull{it.id==id}?:cars.first()
}
