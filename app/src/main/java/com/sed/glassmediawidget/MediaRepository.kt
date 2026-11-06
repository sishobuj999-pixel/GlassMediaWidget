package com.sed.glassmediawidget

import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager

data class MediaState(val title: String, val artist: String, val playing: Boolean)

object MediaRepository {
    @Volatile private var controller: MediaController? = null

    fun update(value: MediaController?) { controller = value }

    fun current(context: Context): MediaState {
        val c = controller ?: return MediaState("Nothing playing", "Enable notification access", false)
        val metadata = c.metadata
        val playback = c.playbackState
        val title = metadata?.getString(android.media.MediaMetadata.METADATA_KEY_TITLE) ?: "Unknown title"
        val artist = metadata?.getString(android.media.MediaMetadata.METADATA_KEY_ARTIST) ?: c.packageName
        val playing = playback?.state == android.media.session.PlaybackState.STATE_PLAYING
        return MediaState(title, artist, playing)
    }

    fun active(context: Context): MediaController? = controller
}
