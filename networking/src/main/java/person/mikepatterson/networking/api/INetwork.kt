package person.mikepatterson.networking.api

import person.mikepatterson.common_utils.ApiResult

interface INetwork {

    suspend fun <T> makeRequestForPojo(request: NetworkRequest) : ApiResult<T, NetworkException>
}
