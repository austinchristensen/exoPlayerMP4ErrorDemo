package person.mikepatterson.common_utils.methods

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun onMainThread(block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block() }
}
