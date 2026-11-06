package com.sed.glassmediawidget
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
class GlassMediaWidgetProvider : AppWidgetProvider() {
 override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) { ids.forEach { id ->
  val views=RemoteViews(context.packageName,R.layout.widget_glass_media)
  val launch=PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
  views.setOnClickPendingIntent(R.id.play_pause,launch)
  manager.updateAppWidget(id,views)
 }}
}
