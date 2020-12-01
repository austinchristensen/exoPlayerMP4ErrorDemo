package person.mikepatterson.view_models

import person.mikepatterson.models.view_state.IViewState

interface IViewModel<T : IViewState> {

    fun subscribe(render: (T) -> Unit)

    fun unsubscribe()
}
