package com.sed.glassmediawidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class GlassMediaWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { update(context, manager, it) }
    }

    private fun update(context: Context, manager: AppWidgetManager, id: Int) {
        val state = MediaRepository.current(context)
        val views = RemoteViews(context.packageName, R.layout.widget_glass_media)
        views.setTextViewText(R.id.title, state.title)
        views.setTextViewText(R.id.artist, state.artist)
        views.setTextViewText(R.id.play_pause, if (state.playing) "Ⅱ" else "▶")
        views.setOnClickPendingIntent(R.id.previous, action(context, MediaActionReceiver.ACTION_PREVIOUS))
        views.setOnClickPendingIntent(R.id.play_pause, action(context, MediaActionReceiver.ACTION_PLAY_PAUSE))
        views.setOnClickPendingIntent(R.id.next, action(context, MediaActionReceiver.ACTION_NEXT))
        manager.updateAppWidget(id, views)
    }

    private fun action(context: Context, action: String): PendingIntent {
        val intent = Intent(context, MediaActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(context, action.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    companion object {
        fun refresh(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, GlassMediaWidgetProvider::class.java)
            manager.getAppWidgetIds(component).forEach { id ->
                val state = MediaRepository.current(context)
                val views = RemoteViews(context.packageName, R.layout.widget_glass_media)
                views.setTextViewText(R.id.title, state.title)
                views.setTextViewText(R.id.artist, state.artist)
                views.setTextViewText(R.id.play_pause, if (state.playing) "Ⅱ" else "▶")
                manager.updateAppWidget(id, views)
            }
        }
    }
}
