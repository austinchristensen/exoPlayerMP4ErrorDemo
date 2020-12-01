package person.mikepatterson.player.exoplayer

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer

internal class DefaultExoPlayerFactory(private val context: Context) {

    fun createExoPlayer(): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context).build()
    }
}
