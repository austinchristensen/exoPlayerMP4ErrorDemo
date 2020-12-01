package person.mikepatterson.videodemo.dependency_injection

import org.koin.core.module.Module
import org.koin.dsl.module
import person.mikepatterson.common_utils.time.Seconds
import person.mikepatterson.dependency_injection.IModuleProvider
import person.mikepatterson.networking.api.config.NetworkConfiguration

internal class AppModuleProvider : IModuleProvider {

    // example of overriding default network behavior from the app's level
    private val defaultNetworkConfig = module { single { NetworkConfiguration(Seconds(15)) } }

    override fun provideModules(): List<Module> {
        return listOf(defaultNetworkConfig)
    }
}
