package person.mikepatterson.view_models

import android.content.Intent
import android.view.TextureView
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.*
import person.mikepatterson.common_utils.android.SystemUiVizChanged
import person.mikepatterson.common_utils.time.MidstUnit
import person.mikepatterson.common_utils.time.Milliseconds
import person.mikepatterson.common_utils.time.Seconds
import person.mikepatterson.common_utils.video.isHlsUrl
import person.mikepatterson.common_utils.video.isValidVideoUrl
import person.mikepatterson.models.api.StreamingFormat
import person.mikepatterson.models.view_state.*
import person.mikepatterson.player.DemoPlaybackState
import person.mikepatterson.player.LifeCycleAwarePlayer
import person.mikepatterson.view_models.utils.MainThreadRenderer

class PlaybackViewModel(private val player: LifeCycleAwarePlayer) :
    IViewModel<PlaybackViewState>, SystemUiVizChanged {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var controlsTimerJob: Job? = null

    private val renderer = MainThreadRenderer<PlaybackViewState>()

    private val stateManager = PlaybackStateManager()

    private lateinit var name: String
    private lateinit var url: String

    fun preparePlayback(intent: Intent, lifecycle: Lifecycle, textureView: TextureView) {
        name = intent.getStringExtra(VIDEO_NAME) ?: ""
        url = intent.getStringExtra(VIDEO_URL) ?: ""
        val format = if (url.isHlsUrl()) {
            StreamingFormat.HLS
        } else {
            StreamingFormat.DASH
        }
        player.attachLifecycleForPlayback(lifecycle, url, format)
        player.attachTextureView(textureView)
        player.addErrorHandler { renderer(PlaybackErrorViewState(it)) }
        player.addNewStateListener {
            // for now just handle FINISHED
            // TODO: also showUI when necessary cause of buffering starts
            if (it == DemoPlaybackState.FINISHED) {
                renderer(PlaybackFinishedViewState)
            }
        }
    }

    override fun subscribe(render: (PlaybackViewState) -> Unit) {
        if (!url.isValidVideoUrl()) {
            // can't do anything with this...
            render(PlaybackErrorViewState(GENERIC_PLAYBACK_ERROR))
            return
        }
        renderer.block = render
        stateManager.areControlsVisible = true
        startUpdatingStatus()
    }

    override fun unsubscribe() {
        renderer.block = null
        stateManager.areControlsVisible = false
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun startUpdatingStatus() {
        coroutineScope.launch {
            // this "infinite" while loop is safe because `coroutineScope.coroutineContext.cancelChildren()`
            // will cancel out of the coroutine cause [delay] is cancellable
            // recursion is _not_ safe as a coroutine update pattern cause of StackOverflow exceptions
            while (stateManager.areControlsVisible) {
                renderer(buildPlaybackControlsViewState())
                val playbackState = player.getPlaybackStatus()
                if (playbackState != stateManager.previousPlaybackState) {
                    stateManager.previousPlaybackState = player.getPlaybackStatus()
                    maybeBeginControlsTimer(playbackState)
                }
                delay(UPDATE_CONTROLS_INTERVAL.asMilliseconds())
            }
        }
    }

    private fun maybeBeginControlsTimer(playbackState: DemoPlaybackState) {
        if (playbackState == DemoPlaybackState.PLAYING) {
            controlsTimerJob?.cancel()
            startControlsTimer()
        } else {
            controlsTimerJob?.cancel()
        }
    }

    private fun startControlsTimer() {
        controlsTimerJob = coroutineScope.launch {
            delay(CONTROLS_VISIBILITY_DURATION.asMilliseconds())
            hideUiAction()
        }
    }

    override fun showUiAction() {
        if (!stateManager.areControlsVisible) {
            stateManager.areControlsVisible = true
            startUpdatingStatus()
        }
    }

    override fun hideUiAction() {
        if (stateManager.areControlsVisible) {
            stateManager.areControlsVisible = false
            renderer(buildPlaybackControlsViewState())
        }
    }

    private fun buildPlaybackControlsViewState(): PlaybackControlsViewState {
        return if (stateManager.areControlsVisible) {
            val playbackState = player.getPlaybackStatus()
            if (playbackState == DemoPlaybackState.IDLE || playbackState == DemoPlaybackState.BUFFERING) {
                PlaybackControlsViewState.Buffering(buildPlaybackInfo(), buildControlsListener())
            } else if (playbackState == DemoPlaybackState.PLAYING) {
                PlaybackControlsViewState.Playing(buildPlaybackInfo(), buildControlsListener())
            } else {
                PlaybackControlsViewState.Paused(buildPlaybackInfo(), buildControlsListener())
            }
        } else {
            PlaybackControlsViewState.Hidden(buildPlaybackInfo(), buildControlsListener())
        }
    }

    private fun buildPlaybackInfo(): PlaybackInfoViewState {
        return PlaybackInfoViewState(
            player.getCurrentPosition(),
            player.getBufferingPosition(),
            player.getDuration()
        )
    }

    private fun buildControlsListener(): PlaybackControlsListener {
        return PlaybackControlsListener({ onPlayPauseClick() },
            seekToPositionPercentage = { seekToPercentage(it) },
            rewindTenSeconds = { seekFromCurrentPosition(Seconds(10), false) },
            forwardTenSeconds = { seekFromCurrentPosition(Seconds(10), true) }
        )
    }

    private fun onPlayPauseClick() {
        if (player.getPlaybackStatus() == DemoPlaybackState.PLAYING) {
            player.pause()
        } else if (player.getPlaybackStatus() == DemoPlaybackState.PAUSED) {
            player.play()
        }
    }

    private fun seekToPercentage(percentage: Float) {
        val durationMs = player.getDuration().asMilliseconds()
        val adjustedPositionMs = durationMs * percentage
        player.seekTo(Milliseconds(adjustedPositionMs))
    }

    private fun seekFromCurrentPosition(time: MidstUnit, forward: Boolean) {
        val currentPositionMs = player.getCurrentPosition().asMilliseconds()
        val adjustedPositionMs = if (forward) {
            currentPositionMs + time.asMilliseconds()
        } else {
            currentPositionMs - time.asMilliseconds()
        }
        player.seekTo(Milliseconds(adjustedPositionMs))
    }

    companion object PlaybackObject {
        const val VIDEO_ID = "PlaybackActivity.video_id"
        const val VIDEO_NAME = "PlaybackActivity.video_name"
        const val VIDEO_URL = "PlaybackActivity.video_url"

        // TODO: this could be configurable
        private val UPDATE_CONTROLS_INTERVAL = Milliseconds(333)
        private val CONTROLS_VISIBILITY_DURATION = Seconds(5)
        private const val GENERIC_PLAYBACK_ERROR = "Something wrong occurred."
    }
}

// as much as I'd like to avoid a state machine, video playback inevitably forces it
private data class PlaybackStateManager(
    var areControlsVisible: Boolean = false,
    var previousPlaybackState: DemoPlaybackState = DemoPlaybackState.IDLE
)
