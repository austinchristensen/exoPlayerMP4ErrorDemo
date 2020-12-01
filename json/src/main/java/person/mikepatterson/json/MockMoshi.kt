package person.mikepatterson.json

import person.mikepatterson.common_utils.Success
import person.mikepatterson.models.api.*

// So... this is a hasty solution that doesn't actually use moshi or any JSON parsing
// but it keeps the networking module more clean and is in line with the direction that I'm heading
object MockMoshi {

    private fun buildSuccessfulAssetList(): AssetListModel {
        return AssetListModel(
            listOf(
                Asset(
                    "01",
                    "Avocado Commercial",
                    "https://static.realeyes.cloud/android/cmaf2/master_cmaf.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "02",
                    "Big Buck Bunny Clip",
                    "https://static.realeyes.cloud/android/cmaf/master_cmaf.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                )
            )
        )
    }

    private fun buildVeryLongSuccessfulAssetList(): AssetListModel {
        return AssetListModel(
            listOf(
                Asset(
                    "01",
                    "Sintel",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "02",
                    "Big Buck Bunny",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "03",
                    "Sintel-2",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "04",
                    "Big Buck Bunny-2",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "05",
                    "Sintel-3",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "06",
                    "Big Buck Bunny-3",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "07",
                    "Sintel-4",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "08",
                    "Big Buck Bunny-4",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "09",
                    "Sintel-5",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "10",
                    "Big Buck Bunny-5",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "11",
                    "Sintel-6",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "12",
                    "Big Buck Bunny-6",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "13",
                    "Sintel-7",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    PlaybackConfig(
                        StreamingFormat.HLS,
                        DrmType.NONE,
                        AdType.NONE
                    )
                ),
                Asset(
                    "14",
                    "Big Buck Bunny-7",
                    "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-common_init.mpd",
                    PlaybackConfig(
                        StreamingFormat.DASH,
                        DrmType.NONE,
                        AdType.NONE
                    )
                )
            )
        )
    }

    private fun buildEmptyAssetList(): AssetListModel {
        return AssetListModel(emptyList())
    }

    // TODO: could use feature toggles here inside the app to quickly toggle between different expected results from the mock
    // obviously not the right solution - this feels gross to add
    fun returnAssetList(): Success<AssetListModel> {
        return Success(buildSuccessfulAssetList())
    }
}
