package person.mikepatterson.networking.api

import person.mikepatterson.common_utils.methods.debugRequire
import person.mikepatterson.common_utils.methods.isDebugBuild
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketException
import javax.net.ssl.SSLHandshakeException

// The idea here is that we want to know exactly what kind of errors we're expecting from the networking layer
// goal is to avoid catching all exceptions without understanding the root causes
// TODO: the player needs something similar
sealed class NetworkException(val msg: String) : Throwable(msg)

class NetworkInterruptedIOException(val e: InterruptedIOException) : NetworkException(MESSAGE) {

    companion object {
        private const val MESSAGE = "The network request has timed out."
    }
}

object NetworkInvalidRequestException : NetworkException(NoOpNetworkRequest.ERROR_MSG)

class NetworkIoException(val e: IOException) : NetworkException(MESSAGE) {

    companion object {
        private const val MESSAGE = "The network request has failed."
    }
}

class NetworkSocketException(val e: SocketException) : NetworkException(MESSAGE) {

    companion object {
        private const val DEBUG_MESSAGE =
            "The network request has failed. Possibly check that NETWORK ACCESS is enabled in the manifest."
        private const val PROD_MESSAGE = "The network request has failed."
        private val MESSAGE = if (isDebugBuild()) DEBUG_MESSAGE else PROD_MESSAGE
    }
}

class NetworkSSLHandshakeException(val e: SSLHandshakeException) : NetworkException(MESSAGE) {

    companion object {
        private const val DEBUG_MESSAGE =
            "The network request has failed due to an SSL Exception. Possibly check your proxy settings and your device's certificates."
        private const val PROD_MESSAGE = "The network request has failed due to an SSL Exception."
        private val MESSAGE = if (isDebugBuild()) DEBUG_MESSAGE else PROD_MESSAGE
    }
}

class UnexpectedNetworkException(e: Throwable) : NetworkException(MESSAGE) {

    init {
        debugRequire(false, DEBUG_MESSAGE, e)
    }

    companion object {
        private const val DEBUG_MESSAGE =
            "The network request has failed due to an unknown exception. Please add this to the expected exceptions in NetworkException."
        private const val PROD_MESSAGE = "The network request has failed."
        private val MESSAGE = if (isDebugBuild()) DEBUG_MESSAGE else PROD_MESSAGE
    }
}

fun Throwable.toNetworkException(): NetworkException {
    return when (this) {
        // start narrow -> go broad
        is InterruptedIOException -> NetworkInterruptedIOException(this)
        is SocketException -> NetworkSocketException(this)
        is SSLHandshakeException -> NetworkSSLHandshakeException(this)
        is IOException -> NetworkIoException(this)
        // cover the use case of it being a network exception
        is NetworkException -> this
        // unexpected throwable's should be added as a class and included above here in those regions
        else -> UnexpectedNetworkException(this)
    }
}
