package person.mikepatterson.player

import android.view.TextureView
import person.mikepatterson.common_utils.time.MidstUnit
import person.mikepatterson.models.api.StreamingFormat

interface IPlayer {

    fun attachTextureView(textureView: TextureView)

    fun startLoading(url: String, format: StreamingFormat)

    fun play()

    fun pause()

    fun stop()

    fun release()

    fun seekTo(position: MidstUnit)

    fun getPlaybackStatus(): DemoPlaybackState

    fun getCurrentPosition(): MidstUnit

    fun getBufferingPosition(): MidstUnit

    fun getDuration(): MidstUnit

    // lots more to expose here but for now
    fun addErrorHandler(handleError: (String) -> Unit)

    fun addNewStateListener(handleNewState: (DemoPlaybackState) -> Unit)
}
