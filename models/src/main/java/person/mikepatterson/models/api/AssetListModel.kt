package person.mikepatterson.models.api

// this file is crowded, but the concept is simple and kotlin is concise, can be separated out inside the module as the project expands

data class AssetListModel(val assetList: List<Asset>)

data class Asset(val id: String, val name: String, val url: String, val assetType: PlaybackConfig)

data class PlaybackConfig(val streamingFormat: StreamingFormat, val drmType: DrmType, val adType: AdType)

// I think these enums belong somewhere else
enum class StreamingFormat {
    HLS, DASH
}

enum class DrmType {
    NONE
}

enum class AdType {
    NONE
}
