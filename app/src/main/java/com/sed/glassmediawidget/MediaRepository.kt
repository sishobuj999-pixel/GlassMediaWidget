package com.sed.glassmediawidget

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState

data class MediaState(val title: String, val artist: String, val playing: Boolean)

object MediaRepository {
    @Volatile private var controller: MediaController? = null

    fun update(value: MediaController?) {
        controller = value
    }

    private fun activeController(context: Context): MediaController? {
        controller?.let { return it }
        return try {
            val manager = context.getSystemService(MediaSessionManager::class.java) ?: return null
            val listener = ComponentName(context, MediaNotificationListener::class.java)
            manager.getActiveSessions(listener)
                .firstOrNull { it.playbackState?.state == PlaybackState.STATE_PLAYING }
                ?: manager.getActiveSessions(listener).firstOrNull()
        } catch (_: SecurityException) {
            null
        } catch (_: IllegalStateException) {
            null
        }
    }

    fun current(context: Context): MediaState {
        val c = activeController(context)
            ?: return MediaState("Nothing playing", "Enable notification access", false)
        val metadata = c.metadata
        val title = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "Unknown title"
        val artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: c.packageName
        val playing = c.playbackState?.state == PlaybackState.STATE_PLAYING
        return MediaState(title, artist, playing)
    }

    fun active(context: Context): MediaController? = activeController(context)
}
