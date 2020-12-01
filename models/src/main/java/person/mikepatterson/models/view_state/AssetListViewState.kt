package person.mikepatterson.models.view_state

sealed class AssetListViewState : IViewState

data class AssetListSuccessState(val list: List<AssetListItemViewState>) : AssetListViewState()

sealed class AssetListFailureState : AssetListViewState() {

    data class IO(val msg: String) : AssetListFailureState()

    data class InvalidRequest(val msg: String) : AssetListFailureState()

    object EmptyList : AssetListFailureState() {

        const val EMPTY_LIST_MSG = "There were no found assets. Please try again."
    }
}

sealed class AssetListItemViewState {

    data class AssetHeader(val title: String) : AssetListItemViewState()

    data class AssetCard(val id: String, val name: String, val onClick: () -> Unit) : AssetListItemViewState()
}

data class StartPlaybackState(val id: String, val name: String, val url: String) : AssetListViewState()
