package person.mikepatterson.dependency_injection

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Create in the Application's onCreate method
 */
// Note: Too tightly coupled to Koin API's at the moment, would like to decouple this module's API from the underlying engine but Koin doesn't make this easy
class DependencyInjector(private val context: Context, private val modules: List<Module>) {

    init {
        startKoin {
            androidContext(context)
            modules(modules)
        }
    }
}
