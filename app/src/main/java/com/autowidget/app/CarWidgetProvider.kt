package com.autowidget.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class CarWidgetProvider: AppWidgetProvider() {
    override fun onUpdate(context:Context, manager:AppWidgetManager, ids:IntArray) {
        ids.forEach { render(context,manager,it) }
        scheduleNext(context)
    }
    override fun onReceive(context:Context,intent:Intent) {
        super.onReceive(context,intent)
        if(intent.action==ACTION_NEXT_FRAME) {
            val manager=AppWidgetManager.getInstance(context)
            val ids=manager.getAppWidgetIds(ComponentName(context,CarWidgetProvider::class.java))
            ids.forEach { render(context,manager,it) }
            scheduleNext(context)
        }
    }
    companion object {
        const val ACTION_NEXT_FRAME="com.autowidget.app.NEXT_FRAME"
        fun render(context:Context,manager:AppWidgetManager,id:Int) {
            val prefs=context.getSharedPreferences("auto_widget",Context.MODE_PRIVATE)
            val ids=CarCatalog.cars.map{it.id}
            val selected=prefs.getString("car_$id","sports") ?: "sports"
            val animated=prefs.getBoolean("animated_$id",false)
            val car=if(animated) {
                val index=((System.currentTimeMillis()/4500L)%ids.size).toInt()
                CarCatalog.byId(ids[index])
            } else CarCatalog.byId(selected)
            val views=RemoteViews(context.packageName,R.layout.car_widget)
            views.setImageViewResource(R.id.car_art,car.art)
            manager.updateAppWidget(id,views)
        }
        private fun scheduleNext(context:Context) {
            val prefs=context.getSharedPreferences("auto_widget",Context.MODE_PRIVATE)
            if(!prefs.getBoolean("animate_any",false)) return
            val intent=Intent(context,CarWidgetProvider::class.java).setAction(ACTION_NEXT_FRAME)
            val pending=PendingIntent.getBroadcast(context,7001,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val alarm=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm.set(AlarmManager.RTC, System.currentTimeMillis()+4500L,pending)
        }
    }
}