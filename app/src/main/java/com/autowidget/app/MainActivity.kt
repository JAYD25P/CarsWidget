package com.autowidget.app

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink=Color(0xFF090D12); private val Panel=Color(0xFF151C24); private val Panel2=Color(0xFF1D2732); private val Orange=Color(0xFFFF6B35); private val Muted=Color(0xFF9AA8B7); private val White=Color(0xFFF4F7FA)
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{AutoWidgetScreen(this)}}}

@Composable fun AutoWidgetScreen(context:Context){
 val prefs=context.getSharedPreferences("auto_widget",Context.MODE_PRIVATE)
 var selected by remember{mutableStateOf(prefs.getString("default_car","spectral")?:"spectral")}
 var animated by remember{mutableStateOf(prefs.getBoolean("animate_any",false))}
 var layout by remember{mutableStateOf(prefs.getString("layout_mode","single")?:"single")}
 var category by remember{mutableStateOf("All")};var style by remember{mutableStateOf("All styles")};var query by remember{mutableStateOf("")};var tab by remember{mutableStateOf("Discover")}
 val categories=listOf("All","Performance","Motorsport","SUV","Classic","Utility","Commercial","Electric")
 val chosen=CarCatalog.byId(selected)
 val shown=CarCatalog.cars.filter{(category=="All"||it.category==category)&&(style=="All styles"||it.style==style)&&(query.isBlank()||it.name.contains(query,true)||it.category.contains(query,true))}
 MaterialTheme(colorScheme=darkColorScheme(primary=Orange,background=Ink,surface=Panel,onBackground=White,onSurface=White)){
  Scaffold(containerColor=Ink,bottomBar={NavigationBar(containerColor=Color(0xFF111820),contentColor=White,tonalElevation=0.dp){
   listOf("Discover","Widget","Library").forEachIndexed{i,label->val icon=when(i){0->Icons.Rounded.Explore;1->Icons.Rounded.Widgets;else->Icons.Rounded.Collections}
    NavigationBarItem(selected=tab==label,onClick={tab=label},icon={Icon(icon,null)},label={Text(label)},colors=NavigationBarItemDefaults.colors(selectedIconColor=Orange,selectedTextColor=Orange,indicatorColor=Orange.copy(alpha=.13f),unselectedIconColor=Muted,unselectedTextColor=Muted))
   }
  }}){pad->
   Column(Modifier.fillMaxSize().padding(pad)){
    Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=15.dp),verticalAlignment=Alignment.CenterVertically){
     Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Orange,Color(0xFFB92D19)))),contentAlignment=Alignment.Center){Icon(Icons.Rounded.DirectionsCar,null,tint=White,modifier=Modifier.size(25.dp))}
     Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text("MOTORVAULT",fontSize=18.sp,fontWeight=FontWeight.Black,letterSpacing=1.8.sp,color=White);Text("AUTOMOTIVE ART COLLECTION",fontSize=9.sp,letterSpacing=1.2.sp,color=Muted)}
     IconButton(onClick={tab="Widget"}){Icon(Icons.Rounded.Tune,"Widget settings",tint=White)}
    }
    when(tab){
     "Widget"->WidgetStudio(selected,animated,layout,{animated=it;prefs.edit().putBoolean("animate_any",it).apply()},{layout=it;prefs.edit().putString("layout_mode",it).apply()},{
      prefs.edit().putString("default_car",selected).putString("layout_mode",layout).apply()
      val mgr=AppWidgetManager.getInstance(context);mgr.getAppWidgetIds(ComponentName(context,CarWidgetProvider::class.java)).forEach{CarWidgetProvider.render(context,mgr,it)};CarWidgetProvider.scheduleAnimation(context,animated)
     })
     "Library"->LibraryScreen()
     else->Column(Modifier.fillMaxSize().padding(horizontal=18.dp)){
      Text("THE GARAGE",fontSize=11.sp,fontWeight=FontWeight.Bold,letterSpacing=2.sp,color=Orange)
      Text("Find your next\nwallpaper-worthy ride.",fontSize=29.sp,lineHeight=34.sp,fontWeight=FontWeight.Bold,color=White)
      Spacer(Modifier.height(15.dp));FeaturedVehicle(chosen);Spacer(Modifier.height(17.dp))
      Row(verticalAlignment=Alignment.CenterVertically){Text("Explore collection",fontSize=19.sp,fontWeight=FontWeight.Bold,color=White,modifier=Modifier.weight(1f));Text("${shown.size} VEHICLES",fontSize=10.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp,color=Muted)}
      Spacer(Modifier.height(9.dp))
      Row(Modifier.fillMaxWidth().height(46.dp).clip(RoundedCornerShape(14.dp)).background(Panel).padding(horizontal=14.dp),verticalAlignment=Alignment.CenterVertically){
       Icon(Icons.Rounded.Search,null,tint=Muted);Spacer(Modifier.width(10.dp))
       BasicTextField(value=query,onValueChange={query=it},singleLine=true,textStyle=MaterialTheme.typography.bodyMedium.copy(color=White),modifier=Modifier.weight(1f),decorationBox={inner->if(query.isEmpty())Text("Search cars, classes, styles…",color=Muted);inner()})
       if(query.isNotEmpty())IconButton(onClick={query=""},modifier=Modifier.size(28.dp)){Icon(Icons.Rounded.Close,null,tint=Muted)}
      }
      Spacer(Modifier.height(8.dp))
      Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){
       categories.forEach{c->FilterChip(selected=category==c,onClick={category=c},label={Text(c)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Orange.copy(alpha=.18f),selectedLabelColor=Orange,containerColor=Panel,labelColor=Muted),border=FilterChipDefaults.filterChipBorder(enabled=true,selected=category==c,borderColor=Panel2,selectedBorderColor=Orange.copy(alpha=.4f)))}
      }
      Spacer(Modifier.height(6.dp))
      Row(horizontalArrangement=Arrangement.spacedBy(7.dp)){listOf("All styles","Pixel art","Realistic 3D").forEach{s->AssistChip(onClick={style=s},label={Text(s)},leadingIcon={if(style==s)Icon(Icons.Rounded.Check,null,Modifier.size(14.dp))},colors=AssistChipDefaults.assistChipColors(containerColor=if(style==s)Orange.copy(alpha=.16f)else Panel,labelColor=if(style==s)Orange else Muted,leadingIconContentColor=Orange),border=null)}}
      Spacer(Modifier.height(9.dp))
      LazyVerticalGrid(columns=GridCells.Fixed(2),contentPadding=PaddingValues(bottom=14.dp),horizontalArrangement=Arrangement.spacedBy(11.dp),verticalArrangement=Arrangement.spacedBy(11.dp),modifier=Modifier.weight(1f)){
       items(shown,key={it.id}){car->VehicleTile(car,selected==car.id){selected=car.id;prefs.edit().putString("default_car",car.id).apply()}}
      }
     }
    }
   }
  }
 }
}

@Composable private fun FeaturedVehicle(car:CarModel){
 Card(shape=RoundedCornerShape(23.dp),colors=CardDefaults.cardColors(containerColor=Panel),modifier=Modifier.fillMaxWidth()){
  Box(Modifier.fillMaxWidth().height(184.dp)){
   Image(painterResource(car.art),null,Modifier.fillMaxSize(),contentScale=ContentScale.Fit)
   Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Ink.copy(alpha=.9f)))))
   Column(Modifier.align(Alignment.BottomStart).padding(15.dp)){Text("FEATURED MACHINE",fontSize=9.sp,fontWeight=FontWeight.Bold,letterSpacing=1.8.sp,color=Orange);Text(car.name,fontSize=22.sp,fontWeight=FontWeight.Bold,color=White);Text(car.subtitle,fontSize=11.sp,color=Muted)}
   Surface(Modifier.align(Alignment.TopEnd).padding(11.dp),shape=RoundedCornerShape(18.dp),color=Ink.copy(alpha=.75f)){Text(car.style.uppercase(),Modifier.padding(horizontal=9.dp,vertical=6.dp),fontSize=9.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp,color=White)}
  }
 }
}
@Composable private fun VehicleTile(car:CarModel,selected:Boolean,onClick:()->Unit){
 Card(onClick=onClick,shape=RoundedCornerShape(17.dp),colors=CardDefaults.cardColors(containerColor=if(selected)Color(0xFF252D35)else Panel),border=if(selected)androidx.compose.foundation.BorderStroke(1.dp,Orange.copy(alpha=.65f))else null){
  Column(Modifier.padding(9.dp)){Box(Modifier.fillMaxWidth().height(94.dp).clip(RoundedCornerShape(12.dp)).background(Panel2),contentAlignment=Alignment.Center){Image(painterResource(car.art),null,Modifier.fillMaxSize().padding(6.dp),contentScale=ContentScale.Fit);if(selected)Icon(Icons.Rounded.CheckCircle,null,tint=Orange,modifier=Modifier.align(Alignment.TopEnd).padding(5.dp).size(18.dp))}
   Spacer(Modifier.height(8.dp));Text(car.name,fontSize=13.sp,fontWeight=FontWeight.SemiBold,color=White,maxLines=1,overflow=TextOverflow.Ellipsis);Text(car.category.uppercase(),fontSize=9.sp,letterSpacing=1.sp,color=Muted)
  }
 }
}
@Composable private fun WidgetStudio(selected:String,animated:Boolean,layout:String,onAnimated:(Boolean)->Unit,onLayout:(String)->Unit,onApply:()->Unit){
 Column(Modifier.fillMaxSize().padding(20.dp)){Text("HOME SCREEN",fontSize=10.sp,letterSpacing=2.sp,fontWeight=FontWeight.Bold,color=Orange);Text("Widget studio",fontSize=29.sp,fontWeight=FontWeight.Bold,color=White);Text("Choose how your collection appears on your launcher.",color=Muted,fontSize=13.sp)
  Spacer(Modifier.height(22.dp));Card(colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(17.dp)){Text("LAYOUT",fontSize=10.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold,color=Muted);Spacer(Modifier.height(10.dp));Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){
   listOf("single" to "Single vehicle","collection" to "3-car strip").forEach{(v,label)->Surface(onClick={onLayout(v)},shape=RoundedCornerShape(13.dp),color=if(layout==v)Orange.copy(alpha=.16f)else Panel2,border=if(layout==v)androidx.compose.foundation.BorderStroke(1.dp,Orange)else null,modifier=Modifier.weight(1f)){Column(Modifier.padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally){Icon(if(v=="single")Icons.Rounded.DirectionsCar else Icons.Rounded.ViewCarousel,null,tint=if(layout==v)Orange else Muted,modifier=Modifier.size(27.dp));Spacer(Modifier.height(7.dp));Text(label,fontSize=11.sp,color=White)}}}
  };Spacer(Modifier.height(16.dp));HorizontalDivider(color=Panel2);Row(Modifier.fillMaxWidth().padding(top=10.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Animated rotation",fontWeight=FontWeight.SemiBold,color=White);Text("Cycle artwork on the widget",fontSize=11.sp,color=Muted)};Switch(checked=animated,onCheckedChange=onAnimated)}}
  }
  Spacer(Modifier.height(15.dp));Card(colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(20.dp)){Column(Modifier.padding(17.dp)){Row(verticalAlignment=Alignment.CenterVertically){Icon(Icons.Rounded.TipsAndUpdates,null,tint=Orange);Spacer(Modifier.width(8.dp));Text("Widget setup",fontWeight=FontWeight.Bold,color=White)};Spacer(Modifier.height(8.dp));Text("Selected vehicle: ${CarCatalog.byId(selected).name}. Apply your settings, then add MOTORVAULT from your launcher's widget picker.",fontSize=12.sp,color=Muted,lineHeight=18.sp)}}
  Spacer(Modifier.weight(1f));Button(onClick=onApply,modifier=Modifier.fillMaxWidth().height(54.dp),shape=RoundedCornerShape(15.dp),colors=ButtonDefaults.buttonColors(containerColor=Orange,contentColor=Ink)){Icon(Icons.Rounded.CheckCircle,null);Spacer(Modifier.width(8.dp));Text("Apply widget settings",fontWeight=FontWeight.Bold)}
 }
}
@Composable private fun LibraryScreen(){
 Column(Modifier.fillMaxSize().padding(20.dp)){Text("YOUR COLLECTION",fontSize=10.sp,letterSpacing=2.sp,fontWeight=FontWeight.Bold,color=Orange);Text("Asset library",fontSize=29.sp,fontWeight=FontWeight.Bold,color=White);Text("Sourced artwork with per-asset licence notes.",color=Muted,fontSize=13.sp);Spacer(Modifier.height(20.dp))
  listOf("Pixel-art sports cars" to "OpenGameArt · CC0 1.0","Spectral GT RS" to "Game-ready GLB · CC0 1.0","Realistic Car Concept" to "Khronos glTF Sample Assets · CC-BY 4.0","Toy Car" to "Khronos glTF Sample Assets · CC0 1.0").forEach{(name,license)->Card(Modifier.fillMaxWidth().padding(bottom=9.dp),colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(15.dp)){Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Rounded.DirectionsCar,null,tint=Orange);Spacer(Modifier.width(12.dp));Column{Text(name,color=White,fontWeight=FontWeight.SemiBold);Text(license,color=Muted,fontSize=11.sp)}}}}
  Spacer(Modifier.height(6.dp));Text("3D models are bundled as source assets. Launcher widgets use pre-rendered images because Android RemoteViews cannot display live GLB scenes.",fontSize=12.sp,color=Muted,lineHeight=18.sp)
 }
}