package com.sed.glassmediawidget

import android.content.Intent
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.service.notification.NotificationListenerService

class MediaNotificationListener : NotificationListenerService() {
    private var manager: MediaSessionManager? = null
    private val callback = object : MediaSessionManager.OnActiveSessionsChangedListener {
        override fun onActiveSessionsChanged(controllers: MutableList<MediaController>?) {
            MediaRepository.update(controllers?.firstOrNull())
            GlassMediaWidgetProvider.refresh(this@MediaNotificationListener)
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        manager = getSystemService(MediaSessionManager::class.java)
        manager?.addOnActiveSessionsChangedListener(callback, null)
        MediaRepository.update(manager?.getActiveSessions(null)?.firstOrNull())
        GlassMediaWidgetProvider.refresh(this)
    }

    override fun onListenerDisconnected() {
        manager?.removeOnActiveSessionsChangedListener(callback)
        manager = null
        super.onListenerDisconnected()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PREVIOUS -> MediaRepository.active(this)?.transportControls?.skipToPrevious()
            ACTION_NEXT -> MediaRepository.active(this)?.transportControls?.skipToNext()
            ACTION_PLAY_PAUSE -> {
                val c = MediaRepository.active(this)
                if (c?.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING) c.transportControls.pause()
                else c?.transportControls?.play()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val ACTION_PREVIOUS = "com.sed.glassmediawidget.PREVIOUS"
        const val ACTION_PLAY_PAUSE = "com.sed.glassmediawidget.PLAY_PAUSE"
        const val ACTION_NEXT = "com.sed.glassmediawidget.NEXT"
    }
}
