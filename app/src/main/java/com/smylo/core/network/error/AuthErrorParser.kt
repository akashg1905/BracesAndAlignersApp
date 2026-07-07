package com.smylo.core.network.error

import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.HttpException

object AuthErrorParser {

  private val gson = Gson()

  /**
   * Returns a user-safe message from auth 400 responses (e.g. "No account found. Please register.").
   * Returns null when the error should fall back to generic network mapping.
   */
  fun parseUserMessage(throwable: Throwable): String? {
    if (throwable !is HttpException || throwable.code() != 400) return null
    val detail = parseDetail(throwable) ?: return null
    if (detail.isBlank() || detail.length > 300) return null
    return detail
  }

  private fun parseDetail(exception: HttpException): String? {
    val body = runCatching { exception.response()?.errorBody()?.string() }.getOrNull()
      ?: return null
    return runCatching {
      val json = gson.fromJson(body, JsonElement::class.java)?.asJsonObject ?: return null
      when {
        json.has("detail") -> formatDetail(json.get("detail"))
        json.has("message") -> json.get("message").asString
        else -> null
      }
    }.getOrNull()
  }

  private fun formatDetail(element: JsonElement): String? = when {
    element.isJsonPrimitive -> element.asString
    element.isJsonArray -> element.asJsonArray.firstOrNull()?.asJsonObject?.get("msg")?.asString
    else -> null
  }
}
