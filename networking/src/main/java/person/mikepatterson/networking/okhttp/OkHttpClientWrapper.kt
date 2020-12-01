package person.mikepatterson.networking.okhttp

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import person.mikepatterson.common_utils.ApiResult
import person.mikepatterson.common_utils.Failure
import person.mikepatterson.common_utils.Success
import person.mikepatterson.common_utils.suspendCoroutineForApiResult
import person.mikepatterson.json.MockMoshi
import person.mikepatterson.networking.api.*
import java.io.IOException
import kotlin.coroutines.resumeWithException

internal class OkHttpClientWrapper(private val callFactory: Call.Factory) : INetwork {

    override suspend fun <T> makeRequestForPojo(request: NetworkRequest): ApiResult<T, NetworkException> {
        return when (request) {
            is ActionableNetworkRequest -> makeNetworkRequest(request)
            is NoOpNetworkRequest -> Failure(request.throwable)
        }
    }

    private suspend fun <T> makeNetworkRequest(request: ActionableNetworkRequest): ApiResult<T, NetworkException>  {
        val okHttpRequest = request.toOkHttpRequest()
        val responseResult = okHttpRequest.executeCall()
        return when (responseResult) {
            is Success -> parseOkHttpResponse(responseResult.result)
            is Failure -> Failure(responseResult.error.toNetworkException())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> parseOkHttpResponse(response: Response): ApiResult<T, NetworkException> {
        // https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/ParseResponseWithMoshi.java
        // this is gross - not what I want here with the forced casting
        return if (response.isSuccessful) {
            MockMoshi.returnAssetList() as Success<T>
        } else {
            Failure(NetworkIoException(IOException("Unexpected network code: ${response.code}")))
        }
    }

    private suspend fun Request.executeCall() = suspendCoroutineForApiResult<Response> { cont ->
        callFactory.newCall(this).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                cont.resumeWith(Result.success(response))
            }

            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }
        })
    }
}
