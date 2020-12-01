package person.mikepatterson.player

enum class DemoPlaybackState {
    IDLE,
    BUFFERING, // could be more specific about why it's buffering: initial loading, seeking happened, network underflow
    PAUSED,
    PLAYING,
    FINISHED
}
