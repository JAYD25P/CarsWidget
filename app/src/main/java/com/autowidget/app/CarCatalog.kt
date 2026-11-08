package com.autowidget.app

data class CarModel(val id:String,val name:String,val category:String,val art:Int)
object CarCatalog {
    val cars = listOf(
        CarModel("sports","Sports coupe","Performance",R.drawable.car_sports),
        CarModel("suv","Adventure SUV","SUV",R.drawable.car_suv),
        CarModel("classic","Vintage classic","Classic",R.drawable.car_classic),
        CarModel("hyper","Hypercar concept","Performance",R.drawable.car_sports),
        CarModel("sedan","Executive sedan","Passenger",R.drawable.car_classic),
        CarModel("offroad","Off-road 4x4","Utility",R.drawable.car_suv),
        CarModel("motorcycle","Sport motorcycle","Two-wheel",R.drawable.car_sports),
        CarModel("truck","Heavy truck","Commercial",R.drawable.car_suv),
        CarModel("racing","Track racer","Motorsport",R.drawable.car_sports),
        CarModel("electric","Electric concept","Electric",R.drawable.car_classic)
    )
    fun byId(id:String)=cars.firstOrNull{it.id==id}?:cars.first()
}