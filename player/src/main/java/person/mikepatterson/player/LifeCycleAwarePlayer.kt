package person.mikepatterson.player

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import person.mikepatterson.models.api.StreamingFormat

class LifeCycleAwarePlayer(private val player: IPlayer) : IPlayer by player, LifecycleObserver {

    private lateinit var url: String
    private lateinit var format: StreamingFormat

    fun attachLifecycleForPlayback(lifeCycle: Lifecycle, url: String, format: StreamingFormat) {
        lifeCycle.addObserver(this)
        this.url = url
        this.format = format
        player.pause()
        player.startLoading(url, format)
    }

    fun updatePlaybackVideo(url: String, format: StreamingFormat) {
        this.url = url
        this.format = format
        stop()
        player.startLoading(url, format)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        player.play()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        player.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        player.release()
    }
}
