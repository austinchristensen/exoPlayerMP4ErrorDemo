package person.mikepatterson.logging

import timber.log.Timber

object DemoLogger {

    /**
     * Init's the logger so it can prepare itself to report the correct tags
     * Should be called in the application's onCreate method
     */
    fun init() {
        Timber.plant(Timber.DebugTree())
    }

    /**
     * Logs a message for debugging
     */
    fun d(msg: String) {
        Timber.d(msg)
    }

    /**
     * Logs a message as a warning
     */
    fun w(msg: String) {
        Timber.w(msg)
    }

    /**
     * Logs a message as an error
     */
    fun e(msg: String) {
        Timber.e(msg)
    }

    /**
     * Logs a message as an error and includes the throwable
     */
    fun e(msg: String, e: Throwable) {
        Timber.e(e, msg)
    }
}
