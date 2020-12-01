package person.mikepatterson.common_utils.methods

import java.io.PrintWriter
import java.io.StringWriter

/**
 * For debug deploys, it's the same as https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html
 * For production deploys, it will do nothing
 */
fun debugRequire(value: Boolean) {
    debugRequire(value, "Failed debug requirement.")
}

fun debugRequire(value: Boolean, msg: String) {
    if (isDebugBuild()) {
        require(value) { msg }
    } else {
        try {
            require(value) { msg }
        } catch (e: IllegalArgumentException) {
            // do nothing - add some analytics reporting or something
        }
    }
}

fun debugRequire(value: Boolean, msg: String, e: Throwable) {
    // shamelessly taken from Timber.prepareLog & Timber.getStackTraceString
    val sw = StringWriter(256)
    val pw = PrintWriter(sw, false)
    e.printStackTrace(pw)
    pw.flush()
    val stackTraceString = sw.toString()
    val errorMsg = "$msg\n$stackTraceString"
    debugRequire(value, errorMsg)
}
