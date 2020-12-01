package person.mikepatterson.player

import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import person.mikepatterson.dependency_injection.IModuleProvider
import person.mikepatterson.player.exoplayer.DefaultExoPlayerFactory
import person.mikepatterson.player.exoplayer.DemoExoPlayerImpl
import person.mikepatterson.player.exoplayer.DemoMediaSourceFactory

class PlayerModuleProvider : IModuleProvider {

    // TODO: now that koin is working here, make bandwidth meter a singleton so that playback
    // starts with a better best-guess at initial bitrate based on the device's previous playback quality
    private val demoMediaSourceFactoryModule = module { factory { DemoMediaSourceFactory(get()) } }
    private val exoPlayerModule =
        module { factory { DefaultExoPlayerFactory(androidContext()).createExoPlayer() } }
    private val exoPlayerImplModule =
        module { factory { DemoExoPlayerImpl(get(), get()) as IPlayer } }
    private val lifeCycleAwarePlayer = module { factory { LifeCycleAwarePlayer(get()) } }
    private val okHttpDataSourceModule =
        module { factory { OkHttpDataSourceFactory(get(), null) as DataSource.Factory } }

    override fun provideModules(): List<Module> {
        return listOf(
            demoMediaSourceFactoryModule,
            exoPlayerModule,
            exoPlayerImplModule,
            lifeCycleAwarePlayer,
            okHttpDataSourceModule
        )
    }
}
