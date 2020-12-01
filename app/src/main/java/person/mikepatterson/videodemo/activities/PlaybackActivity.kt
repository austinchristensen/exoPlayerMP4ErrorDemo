package person.mikepatterson.videodemo.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_playback.*
import org.koin.android.ext.android.inject
import person.mikepatterson.models.view_state.PlaybackControlsViewState
import person.mikepatterson.models.view_state.PlaybackErrorViewState
import person.mikepatterson.models.view_state.PlaybackFinishedViewState
import person.mikepatterson.models.view_state.PlaybackViewState
import person.mikepatterson.videodemo.R
import person.mikepatterson.view_models.PlaybackViewModel


class PlaybackActivity : AppCompatActivity() {

    private val viewModel by inject<PlaybackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)
        systemVizHandler.prepare(window, viewModel)
        viewModel.preparePlayback(intent, lifecycle, videoTextureView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe { render(it) }
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe()
    }

    private fun render(viewState: PlaybackViewState) {
        when (viewState) {
            is PlaybackControlsViewState -> {
                when (viewState) {
                    is PlaybackControlsViewState.Hidden -> systemVizHandler.hideSystemUi(window)
                    else -> systemVizHandler.showSystemUi(window)
                }
                playbackControls.render(viewState)
            }
            is PlaybackErrorViewState -> endPlaybackWithErrorMessage(viewState.msg)
            is PlaybackFinishedViewState -> finish()
        }
    }

    private fun endPlaybackWithErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        finish()
    }
}
