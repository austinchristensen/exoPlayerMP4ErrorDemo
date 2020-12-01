package person.mikepatterson.player.exoplayer

import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import person.mikepatterson.models.api.StreamingFormat

internal class DemoMediaSourceFactory(private val dataSourceFactory: DataSource.Factory) {

    fun getMediaSourceFactory(format: StreamingFormat): MediaSourceFactory {
        return if (format == StreamingFormat.HLS) {
            HlsMediaSource.Factory(dataSourceFactory)
        } else {
            DashMediaSource.Factory(dataSourceFactory)
        }
    }
}
