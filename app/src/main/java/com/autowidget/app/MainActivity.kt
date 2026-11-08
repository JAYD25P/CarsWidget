package com.autowidget.app

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState:Bundle?) { super.onCreate(savedInstanceState); setContent { AutoWidgetScreen(this) } }
}
@Composable
fun AutoWidgetScreen(context:Context) {
    val prefs=context.getSharedPreferences("auto_widget",Context.MODE_PRIVATE)
    var selected by remember { mutableStateOf(prefs.getString("default_car","sports")?:"sports") }
    var animated by remember { mutableStateOf(prefs.getBoolean("animate_any",false)) }
    var layoutMode by remember { mutableStateOf(prefs.getString("layout_mode","single")?:"single") }
    var category by remember { mutableStateOf("All") }
    val categories=listOf("All","Performance","SUV","Classic","Passenger","Utility","Two-wheel","Commercial","Motorsport","Electric")
    val shown=CarCatalog.cars.filter { category=="All" || it.category==category }
    MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFFFF6B35),background=Color(0xFF101114),surface=Color(0xFF1B1D22))) {
        Scaffold(containerColor=Color(0xFF101114)) { pad ->
            Column(Modifier.fillMaxSize().padding(pad).padding(18.dp)) {
                Text("AUTOWIDGET",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Black,color=Color.White)
                Text("Your automotive collection",color=Color.LightGray)
                Spacer(Modifier.height(18.dp))
                Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF202329)),shape=RoundedCornerShape(24.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("WIDGET PREVIEW",color=Color(0xFFFF9A72),style=MaterialTheme.typography.labelLarge)
                        Image(painterResource(CarCatalog.byId(selected).art),null,Modifier.fillMaxWidth().height(130.dp))
                        Text(CarCatalog.byId(selected).name,color=Color.White,fontWeight=FontWeight.SemiBold)
                        Row(verticalAlignment=Alignment.CenterVertically) {
                            Switch(checked=animated,onCheckedChange={animated=it;prefs.edit().putBoolean("animate_any",it).apply()})
                            Text("Animated collection rotation",color=Color.White)
                        }
                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected=layoutMode=="single",onClick={layoutMode="single"},label={Text("Single car")})
                            FilterChip(selected=layoutMode=="collection",onClick={layoutMode="collection"},label={Text("Collection")})
                        }
                        Button(onClick={
                            prefs.edit().putString("default_car",selected).putString("layout_mode",layoutMode).apply()
                            val mgr=AppWidgetManager.getInstance(context)
                            val ids=mgr.getAppWidgetIds(ComponentName(context,CarWidgetProvider::class.java))
                            ids.forEach { CarWidgetProvider.render(context,mgr,it) }
                            CarWidgetProvider.scheduleAnimation(context, animated)
                        },modifier=Modifier.fillMaxWidth()) { Text("Apply to home-screen widgets") }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text("COLLECTION",color=Color.White,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                        categories.forEach { c -> FilterChip(selected=category==c,onClick={category=c},label={Text(c)}) }
                }
                LazyVerticalGrid(columns=GridCells.Fixed(2),contentPadding=PaddingValues(vertical=10.dp),horizontalArrangement=Arrangement.spacedBy(10.dp),verticalArrangement=Arrangement.spacedBy(10.dp),modifier=Modifier.weight(1f)) {
                    items(shown) { car ->
                        Card(onClick={selected=car.id;prefs.edit().putString("default_car",car.id).apply()},colors=CardDefaults.cardColors(containerColor=if(selected==car.id) Color(0xFF39302D) else Color(0xFF202329)),shape=RoundedCornerShape(18.dp)) {
                            Column(Modifier.padding(10.dp),horizontalAlignment=Alignment.CenterHorizontally) {
                                Image(painterResource(car.art),null,Modifier.fillMaxWidth().height(84.dp))
                                Text(car.name,color=Color.White,fontWeight=FontWeight.SemiBold)
                                Text(car.category,color=Color.LightGray,style=MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
                Text("Add AutoWidget from your launcher's widget picker. Resize it to suit your home screen.",color=Color.LightGray,style=MaterialTheme.typography.bodySmall)
            }
        }
    }
}