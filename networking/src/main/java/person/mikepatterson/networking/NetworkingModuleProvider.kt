package person.mikepatterson.networking

import okhttp3.Call
import org.koin.core.module.Module
import org.koin.dsl.module
import person.mikepatterson.dependency_injection.IModuleProvider
import person.mikepatterson.networking.api.INetwork
import person.mikepatterson.networking.api.config.NetworkConfiguration
import person.mikepatterson.networking.okhttp.OkHttpClientWrapper
import person.mikepatterson.networking.okhttp.buildOkHttp

class NetworkingModuleProvider : IModuleProvider {

    private val okHttpModule = module { single { buildOkHttp(getOrNull() ?: NetworkConfiguration()) as Call.Factory } }
    private val networkingModule = module { factory { OkHttpClientWrapper(get()) as INetwork } }

    override fun provideModules(): List<Module> {
        return listOf(okHttpModule, networkingModule)
    }
}
