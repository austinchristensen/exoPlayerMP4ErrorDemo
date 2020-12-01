package person.mikepatterson.videodemo.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.playback_controls_view.view.*
import person.mikepatterson.models.view_state.PlaybackControlsListener
import person.mikepatterson.models.view_state.PlaybackControlsViewState
import person.mikepatterson.models.view_state.PlaybackInfoViewState
import person.mikepatterson.videodemo.R

class PlaybackControlsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    init {
        inflate(context, R.layout.playback_controls_view, this)
    }

    fun render(viewState: PlaybackControlsViewState) {
        when (viewState) {
            is PlaybackControlsViewState.Hidden -> visibility = GONE
            is PlaybackControlsViewState.Buffering -> {
                showBuffering()
            }
            is PlaybackControlsViewState.Playing -> {
                playPause.setImageResource(R.drawable.ic_pause)
                showPlayPause()
            }
            is PlaybackControlsViewState.Paused -> {
                playPause.setImageResource(R.drawable.ic_play_arrow)
                showPlayPause()
            }
        }
        updateSeekbar(viewState.infoViewState)
        setClickListeners(viewState.controlsListener)
    }

    private fun showBuffering() {
        bufferingIndicator.visibility = View.VISIBLE
        playPause.visibility = View.INVISIBLE
        // TODO: it would be nice to animate in the opaque black background
        visibility = View.VISIBLE
    }

    private fun showPlayPause() {
        bufferingIndicator.visibility = View.INVISIBLE
        playPause.visibility = View.VISIBLE
        visibility = View.VISIBLE
    }

    private fun updateSeekbar(infoViewState: PlaybackInfoViewState) {
        playbackSeekbar.progress = msToSeekbarFraction(
            infoViewState.progress.asMilliseconds(),
            infoViewState.duration.asMilliseconds(),
            playbackSeekbar.max
        )
        playbackSeekbar.secondaryProgress = msToSeekbarFraction(
            infoViewState.bufferingProgress.asMilliseconds(),
            infoViewState.duration.asMilliseconds(),
            playbackSeekbar.max
        )
        playbackProgressText.text = infoViewState.progress.asHumanReadableString()
        playbackDurationText.text = infoViewState.duration.asHumanReadableString()
    }

    private fun msToSeekbarFraction(progressMs: Long, durationMs: Long, max: Int): Int {
        return (progressMs * max / durationMs).toInt()
    }

    private fun setClickListeners(controlsListener: PlaybackControlsListener) {
        if (!playPause.hasOnClickListeners()) {
            playPause.setOnClickListener { controlsListener.onPlayPauseClick() }
        }
        if (!tenSecondsRewind.hasOnClickListeners()) {
            tenSecondsRewind.setOnClickListener { controlsListener.rewindTenSeconds() }
        }
        if (!tenSecondsForward.hasOnClickListeners()) {
            tenSecondsForward.setOnClickListener { controlsListener.forwardTenSeconds() }
        }
        if (!playbackSeekbar.hasSeekBarAction()) {
            playbackSeekbar.seekToPositionAction = { controlsListener.seekToPositionPercentage(it) }
        }
    }
}
