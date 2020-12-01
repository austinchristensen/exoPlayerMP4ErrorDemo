package person.mikepatterson.videodemo.views.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import person.mikepatterson.common_utils.android.SystemUiVizChanged

/**
 * This should be the top level view in a layout to see all touch events without other views consuming them
 * It handles the complexity of Android's awful System UI API and exposes system UI changes and touch events through a single callback
 * Classes relying on this must call [prepare] or this class will do nothing
 */
// NOTE: not using MVVM for this view class, I'm sure it could be shoe-horned into it, but I view this mostly as a wrapper around a bad Android API
class SystemVizHandlerView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var callback: SystemUiVizChanged? = null

    fun prepare(window: Window, callback: SystemUiVizChanged?) {
        this.callback = callback
        window.decorView.setOnSystemUiVisibilityChangeListener { viz ->
            // we won't get a touch event because the system UI swallows it
            if (viz and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                this.callback?.showUiAction()
            }
        }
    }

    /**
     * This will notify the callback with showControlsRequested from the window systemUiListener
     */
    fun showSystemUi(window: Window) {
        val uiOptions = View.VISIBLE
        window.decorView.systemUiVisibility = uiOptions
    }

    fun hideSystemUi(window: Window) {
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = uiOptions
        callback?.hideUiAction()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        callback?.showUiAction()
        // we want to capture touch events but we don't want to block them from being propagated down the view hierarchy
        return false
    }
}
