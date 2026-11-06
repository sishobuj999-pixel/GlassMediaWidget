package com.sed.glassmediawidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState

class MediaActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val controller = MediaRepository.active(context) ?: return
        try {
            when (intent.action) {
                ACTION_PREVIOUS -> controller.transportControls.skipToPrevious()
                ACTION_NEXT -> controller.transportControls.skipToNext()
                ACTION_PLAY_PAUSE -> {
                    if (controller.playbackState?.state == PlaybackState.STATE_PLAYING) controller.transportControls.pause()
                    else controller.transportControls.play()
                }
            }
        } catch (_: SecurityException) {
        } catch (_: IllegalStateException) {
        }
        GlassMediaWidgetProvider.refresh(context)
    }

    companion object {
        const val ACTION_PREVIOUS = "com.sed.glassmediawidget.PREVIOUS"
        const val ACTION_PLAY_PAUSE = "com.sed.glassmediawidget.PLAY_PAUSE"
        const val ACTION_NEXT = "com.sed.glassmediawidget.NEXT"
    }
}
