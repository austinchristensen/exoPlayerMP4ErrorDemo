package person.mikepatterson.view_models

import org.koin.core.module.Module
import org.koin.dsl.module
import person.mikepatterson.dependency_injection.IModuleProvider

// TODO: I think perhaps the actual view models could be made internal and only exposed as instances of IViewModel with the right StringQualifiers
class ViewModelModuleProvider : IModuleProvider {

    private val assetListModule = module { factory { AssetListViewModel(get()) } }
    private val playbackModule = module { factory { PlaybackViewModel(get()) } }

    override fun provideModules(): List<Module> {
        return listOf(assetListModule, playbackModule)
    }
}
