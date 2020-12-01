package person.mikepatterson.networking.okhttp

import okhttp3.Headers
import okhttp3.Request
import person.mikepatterson.networking.api.ActionableNetworkRequest
import person.mikepatterson.networking.api.NetworkHeader

internal fun ActionableNetworkRequest.toOkHttpRequest(): Request {
    return Request.Builder()
        .url(url)
        .headers(headers.mapToOkHttpHeaders())
        .build()
}

// not actually performing a map cause of the Headers.Builder pattern
internal fun List<NetworkHeader>.mapToOkHttpHeaders(): Headers {
    val headersBuilder = Headers.Builder()
    forEach { headersBuilder.add(it.name, it.value) }
    return headersBuilder.build()
}
