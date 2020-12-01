package person.mikepatterson.videodemo

import android.app.Application
import person.mikepatterson.dependency_injection.DependencyInjector
import person.mikepatterson.logging.DemoLogger
import person.mikepatterson.videodemo.dependency_injection.DemoModuleProviders

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DemoLogger.init()

        // setup and init DI w/ Koin
        val modules = DemoModuleProviders.provideModules()
        DependencyInjector(this, modules)
    }
}
