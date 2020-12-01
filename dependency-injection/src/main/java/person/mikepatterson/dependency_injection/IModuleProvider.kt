package person.mikepatterson.dependency_injection

import org.koin.core.module.Module

interface IModuleProvider {

    fun provideModules(): List<Module>
}
