package person.mikepatterson.models.view_state

import person.mikepatterson.common_utils.time.MidstUnit

sealed class PlaybackViewState : IViewState

data class PlaybackErrorViewState(val msg: String) : PlaybackViewState()

object PlaybackFinishedViewState : PlaybackViewState()

sealed class PlaybackControlsViewState(
    val infoViewState: PlaybackInfoViewState,
    val controlsListener: PlaybackControlsListener
) : PlaybackViewState() {

    class Hidden(infoViewState: PlaybackInfoViewState, listener: PlaybackControlsListener) :
        PlaybackControlsViewState(infoViewState, listener)

    class Buffering(infoViewState: PlaybackInfoViewState, listener: PlaybackControlsListener) :
        PlaybackControlsViewState(infoViewState, listener)

    class Playing(infoViewState: PlaybackInfoViewState, listener: PlaybackControlsListener) :
        PlaybackControlsViewState(infoViewState, listener)

    class Paused(infoViewState: PlaybackInfoViewState, listener: PlaybackControlsListener) :
        PlaybackControlsViewState(infoViewState, listener)
}

data class PlaybackInfoViewState(
    val progress: MidstUnit,
    val bufferingProgress: MidstUnit,
    val duration: MidstUnit
)

data class PlaybackControlsListener(
    val onPlayPauseClick: () -> Unit,
    val seekToPositionPercentage: (Float) -> Unit,
    val rewindTenSeconds: () -> Unit,
    val forwardTenSeconds: () -> Unit
)
