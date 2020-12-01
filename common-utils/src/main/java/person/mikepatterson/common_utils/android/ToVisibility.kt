package person.mikepatterson.common_utils.android

import android.view.View

fun Boolean.ifTrueVisibleElseGone(): Int {
    return if (this) View.VISIBLE else View.GONE
}

fun Boolean.ifTrueVisibleElseInvisible(): Int {
    return if (this) View.VISIBLE else View.INVISIBLE
}
