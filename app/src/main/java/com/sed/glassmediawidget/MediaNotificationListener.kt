package com.sed.glassmediawidget

import android.content.ComponentName
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.service.notification.NotificationListenerService

class MediaNotificationListener : NotificationListenerService() {
    private var manager: MediaSessionManager? = null

    private val callback = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->
        val selected = controllers?.firstOrNull {
            it.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING
        } ?: controllers?.firstOrNull()
        MediaRepository.update(selected)
        GlassMediaWidgetProvider.refresh(this)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        manager = getSystemService(MediaSessionManager::class.java)
        val listenerComponent = ComponentName(this, MediaNotificationListener::class.java)
        try {
            manager?.addOnActiveSessionsChangedListener(callback, listenerComponent)
            val sessions = manager?.getActiveSessions(listenerComponent).orEmpty()
            MediaRepository.update(
                sessions.firstOrNull { it.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING }
                    ?: sessions.firstOrNull()
            )
        } catch (_: SecurityException) {
            MediaRepository.update(null)
        }
        GlassMediaWidgetProvider.refresh(this)
    }

    override fun onListenerDisconnected() {
        try {
            manager?.removeOnActiveSessionsChangedListener(callback)
        } catch (_: Exception) {
        }
        manager = null
        MediaRepository.update(null)
        super.onListenerDisconnected()
    }
}
