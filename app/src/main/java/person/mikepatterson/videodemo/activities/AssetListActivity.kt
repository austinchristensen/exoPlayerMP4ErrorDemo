package person.mikepatterson.videodemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_asset_list.*
import org.koin.android.ext.android.inject
import person.mikepatterson.models.view_state.AssetListFailureState
import person.mikepatterson.models.view_state.AssetListSuccessState
import person.mikepatterson.models.view_state.AssetListViewState
import person.mikepatterson.models.view_state.StartPlaybackState
import person.mikepatterson.videodemo.R
import person.mikepatterson.view_models.AssetListViewModel
import person.mikepatterson.view_models.PlaybackViewModel

class AssetListActivity : AppCompatActivity() {

    private val viewModel by inject<AssetListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_list)
    }

    override fun onResume() {
        super.onResume()
        updateVisibilityForLoading()
        viewModel.subscribe { render(it) }
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe()
    }

    private fun render(viewState: AssetListViewState) {
        when (viewState) {
            is AssetListSuccessState -> {
                updateVisibilityForSuccess()
                assetListView.render(viewState.list)
            }
            is AssetListFailureState.IO -> {
                errorMessage.text = viewState.msg
                updateVisibilityForError()
            }
            is AssetListFailureState.InvalidRequest -> {
                errorMessage.text = viewState.msg
                updateVisibilityForError()
            }
            AssetListFailureState.EmptyList -> {
                errorMessage.text = AssetListFailureState.EmptyList.EMPTY_LIST_MSG
                updateVisibilityForError()
            }
            is StartPlaybackState -> startPlayback(viewState)
        }
    }

    private fun updateVisibilityForLoading() {
        loadingIndicator.visibility = View.VISIBLE
        assetListView.visibility = View.GONE
        errorMessage.visibility = View.GONE
    }

    private fun updateVisibilityForSuccess() {
        loadingIndicator.visibility = View.GONE
        assetListView.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
    }

    private fun updateVisibilityForError() {
        loadingIndicator.visibility = View.GONE
        assetListView.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
    }

    private fun startPlayback(startPlaybackState: StartPlaybackState) {
        val intent = Intent(this, PlaybackActivity::class.java)
        intent.putExtra(PlaybackViewModel.VIDEO_ID, startPlaybackState.id)
        intent.putExtra(PlaybackViewModel.VIDEO_NAME, startPlaybackState.name)
        intent.putExtra(PlaybackViewModel.VIDEO_URL, startPlaybackState.url)
        startActivity(intent)
    }
}
