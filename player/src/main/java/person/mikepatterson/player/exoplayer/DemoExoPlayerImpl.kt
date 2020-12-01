package person.mikepatterson.player.exoplayer

import android.net.Uri
import android.view.TextureView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import person.mikepatterson.common_utils.time.MidstUnit
import person.mikepatterson.common_utils.time.Milliseconds
import person.mikepatterson.models.api.StreamingFormat
import person.mikepatterson.player.DemoPlaybackState
import person.mikepatterson.player.IPlayer

internal class DemoExoPlayerImpl(
    private val exoPlayer: SimpleExoPlayer,
    private val demoMediaSourceFactory: DemoMediaSourceFactory
) : IPlayer {

    override fun attachTextureView(textureView: TextureView) {
        exoPlayer.setVideoTextureView(textureView)
    }

    // when attaching surface or new video selected (after stop)
    override fun startLoading(url: String, format: StreamingFormat) {
        val mediaSourceFactory = demoMediaSourceFactory.getMediaSourceFactory(format)
        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(url))
        exoPlayer.prepare(mediaSource)
    }

    // onResume or Play Button
    override fun play() {
        exoPlayer.playWhenReady = true
    }

    // onPause or Pause Button
    override fun pause() {
        exoPlayer.playWhenReady = false
    }

    // new video selected (before startLoading)
    override fun stop() {
        exoPlayer.stop()
    }

    // onDestroy
    override fun release() {
        exoPlayer.release()
    }

    override fun seekTo(position: MidstUnit) {
        exoPlayer.seekTo(position.asMilliseconds())
    }

    override fun getPlaybackStatus(): DemoPlaybackState {
        val playerState = exoPlayer.playbackState
        return if (playerState == Player.STATE_IDLE) {
            DemoPlaybackState.IDLE
        } else if (playerState == Player.STATE_BUFFERING) {
            DemoPlaybackState.BUFFERING
        } else if (playerState == Player.STATE_READY) {
            if (exoPlayer.playWhenReady) {
                DemoPlaybackState.PLAYING
            } else {
                DemoPlaybackState.PAUSED
            }
        } else {
            DemoPlaybackState.FINISHED
        }
    }

    override fun getCurrentPosition(): MidstUnit {
        return Milliseconds(exoPlayer.currentPosition)
    }

    override fun getBufferingPosition(): MidstUnit {
        return Milliseconds(exoPlayer.bufferedPosition)
    }

    override fun getDuration(): MidstUnit {
        return Milliseconds(exoPlayer.duration)
    }

    override fun addErrorHandler(handleError: (String) -> Unit) {
        // sloppy, would definitely be an area for improvement
        exoPlayer.addListener(object : Player.EventListener {

            override fun onPlayerError(error: ExoPlaybackException) {
                handleError(GENERIC_EXOPLAYER_MESSAGE)
            }
        })
    }

    override fun addNewStateListener(handleNewState: (DemoPlaybackState) -> Unit) {
        exoPlayer.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                handleNewState(getPlaybackStatus())
            }
        })
    }

    companion object {
        private const val GENERIC_EXOPLAYER_MESSAGE = "Something with ExoPlayer went wrong."
    }
}
