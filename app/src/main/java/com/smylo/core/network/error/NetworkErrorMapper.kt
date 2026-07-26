package com.smylo.core.network.error

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class ErrorCategory(val value: String) {
    NO_INTERNET("no_internet"),
    TIMEOUT("timeout"),
    CONNECTION("connection_failed"),
    SERVER("server_error"),
    AUTH("auth_error"),
    NOT_FOUND("not_found"),
    VALIDATION("validation_error"),
    RATE_LIMIT("rate_limit"),
    PARSE("parse_error"),
    UNKNOWN("unknown")
}

object NetworkErrorMapper {

    fun categorize(throwable: Throwable): ErrorCategory {
        val root = rootCause(throwable)
        return when {
            root is UnknownHostException || throwable is UnknownHostException ->
                ErrorCategory.NO_INTERNET
            root is SocketTimeoutException || throwable is SocketTimeoutException ->
                ErrorCategory.TIMEOUT
            root is ConnectException || throwable is ConnectException ->
                ErrorCategory.CONNECTION
            throwable is HttpException -> when (throwable.code()) {
                401, 403 -> ErrorCategory.AUTH
                404 -> ErrorCategory.NOT_FOUND
                422 -> ErrorCategory.VALIDATION
                429 -> ErrorCategory.RATE_LIMIT
                in 500..599 -> ErrorCategory.SERVER
                else -> ErrorCategory.UNKNOWN
            }
            root is JsonParseException || throwable is JsonParseException ->
                ErrorCategory.PARSE
            root is IOException || throwable is IOException ->
                ErrorCategory.NO_INTERNET
            else -> ErrorCategory.UNKNOWN
        }
    }

    fun toUserMessage(throwable: Throwable): String = when (categorize(throwable)) {
        ErrorCategory.NO_INTERNET ->
            "No internet connection. Please check your network and try again."
        ErrorCategory.TIMEOUT ->
            "The request timed out. Please check your connection and try again."
        ErrorCategory.CONNECTION ->
            "Unable to reach the server. Please try again in a moment."
        ErrorCategory.SERVER ->
            "Our servers are temporarily unavailable. Please try again later."
        ErrorCategory.AUTH ->
            "Your session has expired. Please log in again."
        ErrorCategory.NOT_FOUND ->
            "The requested information could not be found."
        ErrorCategory.VALIDATION ->
            "Some information was invalid. Please review and try again."
        ErrorCategory.RATE_LIMIT ->
            "Too many requests. Please wait a moment and try again."
        ErrorCategory.PARSE ->
            "Could not read the server response. Please try again."
        ErrorCategory.UNKNOWN ->
            "Something went wrong. Please try again."
    }

    private fun rootCause(throwable: Throwable): Throwable {
        var current = throwable
        while (current.cause != null && current.cause !== current) {
            current = current.cause!!
        }
        return current
    }

    /** Stored locally and sent to backend for analysis — never shown to the user. */
    fun toTechnicalDetail(throwable: Throwable): String {
        val builder = StringBuilder()
        builder.append(throwable::class.java.simpleName)
        throwable.message?.let { builder.append(": ").append(it) }

        if (throwable is HttpException) {
            builder.append(" | HTTP ").append(throwable.code())
            runCatching {
                throwable.response()?.errorBody()?.string()?.take(2_000)?.let { body ->
                    builder.append(" | body: ").append(body)
                }
            }
        }

        val root = throwable.cause
        if (root != null && root !== throwable) {
            builder.append(" | cause: ").append(root::class.java.simpleName)
            root.message?.let { builder.append(": ").append(it) }
        }

        return builder.toString()
    }

    fun httpStatus(throwable: Throwable): Int? =
        (throwable as? HttpException)?.code()
}
