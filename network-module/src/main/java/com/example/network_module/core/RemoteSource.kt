package com.networkmodule.core

import com.example.core_module.utils.isValidString
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.Response
object RemoteSource {

    private const val DEFAULT_RETRY_COUNT = 3

    suspend fun <T : Any> safeApiCall(needRetry: Boolean = true, call: suspend () -> Response<T>): Result<T> {
        var count = 0
        var backOffTime = 2_000L
        while (count < DEFAULT_RETRY_COUNT) {
            try {
                val response = call.invoke()
                count = DEFAULT_RETRY_COUNT + 1
                if (response.isSuccessful) {
                    return if (response.body() != null) {
                        Result.Success(response.body()!!)
                    } else {
                        Result.SuccessWithNoContent
                    }
                }
                return handleResponseFailure(response)
            } catch (socketTimeOutException: SocketTimeoutException) {
                return Result.Error(ErrorBody("Unable to connect with the server. Please check your internet connection", ERROR_CODE_TIMEOUT, ""))
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                if (needRetry) {
                    delay(backOffTime)
                    count++
                    backOffTime *= 2
                } else {
                    return Result.Error(ErrorBody("Unable to connect with the server. Please check your internet connection", ERROR_CODE_TIMEOUT, ""))
                }
            } catch (cancellationException: CancellationException) {
                cancellationException.printStackTrace()
                return Result.Error(ErrorBody("", ERROR_CODE_CANCELLATION_JOB, ""))
            }catch (syntaxException: JsonSyntaxException) {
                if (isValidString(syntaxException.message)){
                    return Result.Error(ErrorBody("Parse error : ${syntaxException.message}", -1, ""))
                }else {
                    return Result.Error(ErrorBody("Parse error : $DEFAULT_ERROR_MESSAGE", -1, ""))
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                // An exception was thrown when calling the API so we're converting this to an ErrorBody
                return Result.Error(ErrorBody(DEFAULT_ERROR_MESSAGE, -1, ""))
            }
        }

        return Result.Error(ErrorBody(DEFAULT_ERROR_MESSAGE, -1, ""))
    }

    private fun <T : Any> handleResponseFailure(response: Response<T>): Result.Error {
        val err = response.errorBody()
        if (err != null) {
            val errorDetail = err.string()
            // try getting first error detail
            return try {
                Result.Error(buildErrorModel(response, errorDetail))
                // Keeping old error flow intact
            } catch (ignore: Exception) {
                val responseCode = response.code()
                when {
                    responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR -> Result.Error(ErrorBody("Unable to process your request. Please try again later.", responseCode, errorDetail))
                    responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR + 100 -> Result.Error(ErrorBody("Unable to process your request. Please try again later.", responseCode, errorDetail))
                    else -> Result.Error(ErrorBody("3Something went wrong! Please try again later", responseCode, errorDetail))
                }
            }
        }

        return Result.Error(ErrorBody("2Something went wrong! Please try again later", -1, ""))
    }

    /**
     * Handle response error, default common error view converted, override it to change implementation
     *
     * @param response raw retrofit response
     * @param errorDetail error string from error body
     */
    private fun <T : Any> buildErrorModel(response: Response<T>, errorDetail: String): ErrorBody {

        if (errorDetail.contains("message")) {
            return Gson().fromJson(errorDetail, ErrorBody::class.java).copy(code = response.code(), rawMessage = errorDetail)
        }

        if (!errorDetail.contains("message")) {
            throw IllegalArgumentException()
        }

        // parse the error body from the response, copy the object apply the response code
        return Gson().fromJson(errorDetail, ErrorBody::class.java).copy(code = response.code(), rawMessage = errorDetail)
    }

}