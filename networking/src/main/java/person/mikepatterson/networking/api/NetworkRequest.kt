package person.mikepatterson.networking.api

sealed class NetworkRequest {

    class Builder {

        private var url: String? = null
        private val headers: MutableList<NetworkHeader> = mutableListOf()
        // etc

        fun setUrl(url: String) = apply { this.url = url }

        fun addHeader(header: NetworkHeader) = apply { headers.add(header) }

        fun addHeaders(vararg headers: NetworkHeader) = apply { this.headers.addAll(headers) }

        fun build(): NetworkRequest {
            val request = url?.let {
                if (it.isEmpty()) {
                    null
                } else {
                    ActionableNetworkRequest(it, headers)
                }
            }
            return request ?: NoOpNetworkRequest
        }
    }
}

data class ActionableNetworkRequest(
    val url: String,
    val headers: List<NetworkHeader>
) : NetworkRequest()

object NoOpNetworkRequest : NetworkRequest() {

    internal const val ERROR_MSG = "Improper values to build an actionable network request"

    val throwable = NetworkInvalidRequestException
}
