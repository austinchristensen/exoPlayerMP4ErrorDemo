package person.mikepatterson.common_utils

import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

/**
 * This is a class to return a success or error state from an API
 * It's similar to Result in Haskell, Rust, and Scala
 * It can have extension methods built onto it for easier mapping for success or copy Kotlin [Result] methods like [Result.runCatching]
 *
 * It's somewhat like but it is NOT Kotlin's [Result]
 * Kotlin explicitly didn't want their [Result] class to be able to return from general functions
 * Details on the other [Result] class here:
 * https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md
 */
sealed class ApiResult<out T, out E>

data class Success<out T>(val result: T) : ApiResult<T, Nothing>()
data class Failure<out E>(val error: E) : ApiResult<Nothing, E>()

/**
 * The same as this method but returns [ApiResult] instead of kotlin's [Result]
 * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/run-catching.html
 */
inline fun <T> runCatchingForApiResult(block: () -> T): ApiResult<T, Throwable> {
    return try {
        Success(block())
    } catch (e: Throwable) {
        Failure(e)
    }
}

/**
 * The same as this method but instead of returning T or throwing, it uses [runCatchingForApiResult] to return [ApiResult]
 * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines.experimental/suspend-coroutine.html
 */
suspend inline fun <T> suspendCoroutineForApiResult(crossinline block: (Continuation<T>) -> Unit) =
    runCatchingForApiResult {
        suspendCoroutine<T> { block(it) }
    }
