package person.mikepatterson.view_models.utils

import person.mikepatterson.common_utils.methods.onMainThread

// A util class for other view models to use that ensures the chunk of code executes on the main thread
internal class MainThreadRenderer<T>(var block: ((T) -> Unit)? = null) : (T) -> Unit {

    override fun invoke(p1: T) {
        block?.let {
            onMainThread { it(p1) }
        }
    }
}
