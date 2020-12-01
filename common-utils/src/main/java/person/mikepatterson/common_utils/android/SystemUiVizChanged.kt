package person.mikepatterson.common_utils.android

// this sort of common interface might belong in a separate module like a "definitions" one
interface SystemUiVizChanged {
    fun showUiAction() //  not an ideal name, cause it also covers the case where a ui hide timer should be cancelled and reset
    fun hideUiAction()
}
