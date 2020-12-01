package person.mikepatterson.view_models

import kotlinx.coroutines.*
import person.mikepatterson.common_utils.Failure
import person.mikepatterson.common_utils.Success
import person.mikepatterson.models.api.Asset
import person.mikepatterson.models.api.AssetListModel
import person.mikepatterson.models.api.StreamingFormat
import person.mikepatterson.models.view_state.*
import person.mikepatterson.networking.api.*
import person.mikepatterson.view_models.utils.MainThreadRenderer

class AssetListViewModel(private val networkInterface: INetwork) : IViewModel<AssetListViewState> {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val renderer = MainThreadRenderer<AssetListViewState>()


    override fun subscribe(render: (AssetListViewState) -> Unit) {
        renderer.block = render
        coroutineScope.launch {
            getAssetList()
        }
    }

    override fun unsubscribe() {
        coroutineScope.coroutineContext.cancelChildren()
        renderer.block = null
    }

    private suspend fun getAssetList() {
        delay(1000) // fake delay to show the loading indicator
        val request = buildAssetListRequest()
        val networkingResponse = networkInterface.makeRequestForPojo<AssetListModel>(request)
        val viewState = when (networkingResponse) {
            is Success -> networkingResponse.result.convertToViewState()
            is Failure -> networkingResponse.error.convertToViewState()
        }
        renderer(viewState)
    }

    private fun buildAssetListRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .setUrl("https://media0.giphy.com/media/u0v3z2iQLxyHC/giphy.gif")
            .build()
    }

    private fun AssetListModel.convertToViewState(): AssetListViewState {
        return if (assetList.isEmpty()) {
            AssetListFailureState.EmptyList
        } else {
            val hlsAssetCards =
                assetList.filter { it.assetType.streamingFormat == StreamingFormat.HLS }
                    .map { it.convertToAssetCard() }
            val dashAssetCards =
                assetList.filter { it.assetType.streamingFormat == StreamingFormat.DASH }
                    .map { it.convertToAssetCard() }

            val listItems = mutableListOf<AssetListItemViewState>()
            if (hlsAssetCards.isNotEmpty()) {
                listItems += AssetListItemViewState.AssetHeader(StreamingFormat.HLS.name)
                listItems += hlsAssetCards
            }
            if (dashAssetCards.isNotEmpty()) {
                listItems += AssetListItemViewState.AssetHeader(StreamingFormat.DASH.name)
                listItems += dashAssetCards
            }

            // shouldn't be possible but still handle it
            if (listItems.isEmpty()) {
                AssetListFailureState.EmptyList
            } else {
                AssetListSuccessState(listItems)
            }
        }
    }

    private fun Asset.convertToAssetCard(): AssetListItemViewState.AssetCard {
        return AssetListItemViewState.AssetCard(id, name) { renderer(StartPlaybackState(id, name, url)) }
    }

    private fun NetworkException.convertToViewState(): AssetListViewState {
        return when (this) {
            NetworkInvalidRequestException -> AssetListFailureState.InvalidRequest(msg)
            is NetworkInterruptedIOException,
            is NetworkIoException,
            is NetworkSocketException,
            is NetworkSSLHandshakeException,
            is UnexpectedNetworkException -> AssetListFailureState.IO(msg)
        }
    }
}
