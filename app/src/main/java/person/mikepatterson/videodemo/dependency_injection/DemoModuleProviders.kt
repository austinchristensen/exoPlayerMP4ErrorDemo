package person.mikepatterson.videodemo.dependency_injection

import org.koin.core.module.Module
import person.mikepatterson.dependency_injection.IModuleProvider
import person.mikepatterson.networking.NetworkingModuleProvider
import person.mikepatterson.player.PlayerModuleProvider
import person.mikepatterson.view_models.ViewModelModuleProvider

object DemoModuleProviders : IModuleProvider {

    private val appModuleProvider = AppModuleProvider()
    private val networkingModuleProvider = NetworkingModuleProvider()
    private val playerModuleProvider = PlayerModuleProvider()
    private val viewModelModuleProvider = ViewModelModuleProvider()

    override fun provideModules(): List<Module> {
        return appModuleProvider.provideModules() +
                networkingModuleProvider.provideModules() +
                playerModuleProvider.provideModules() +
                viewModelModuleProvider.provideModules()
    }
}
