package com.task.newsapp.data.util

import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

object NetworkExceptions {

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is IOException -> "No Internet Connection "
            is TimeoutException -> "Server taking too long, try again"
            is HttpException -> {
                when (e.code()) {
                    401 -> "Unauthorized access"
                    403 -> "Server rejected request "
                    404 -> "Resource not found "
                    429 -> "Too many requests, slow down!"
                    500, 502, 503 -> "Server error, try again later ️"
                    else -> "Network error: ${e.code()}"
                }
            }

            else -> "Unknown error occurred "
        }
    }
}