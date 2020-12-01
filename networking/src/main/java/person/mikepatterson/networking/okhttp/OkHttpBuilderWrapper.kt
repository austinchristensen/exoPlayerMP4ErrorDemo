package person.mikepatterson.networking.okhttp

import okhttp3.OkHttpClient
import person.mikepatterson.networking.api.config.NetworkConfiguration

// this could be renamed as factory and made into a class to follow the player factory patterns
internal fun buildOkHttp(networkConfiguration: NetworkConfiguration): OkHttpClient {
    val timeout = networkConfiguration.defaultTimeout
    return OkHttpClient.Builder().callTimeout(timeout.value, timeout.getTimeUnit()).build()
}
