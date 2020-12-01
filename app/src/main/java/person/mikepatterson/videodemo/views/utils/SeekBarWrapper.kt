package person.mikepatterson.videodemo.views.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * A util wrapper around the seekbar to expose a single callback as a higher-order function instead of the actual Android API
 */
class SeekBarWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatSeekBar(context, attrs, defStyle) {

    var seekToPositionAction: ((Float) -> Unit)? = null

    init {
        setOnSeekBarChangeListener(object: OnSeekBarChangeListener {

            private var lastUserSetPosition = 0

            override fun onProgressChanged(seekbar: SeekBar?, position: Int, fromUser: Boolean) {
                if (fromUser) {
                    lastUserSetPosition = position
                }
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                seekToPositionAction?.invoke(lastUserSetPosition / max.toFloat())
            }
        })
    }

    fun hasSeekBarAction(): Boolean {
        return seekToPositionAction != null
    }
}
