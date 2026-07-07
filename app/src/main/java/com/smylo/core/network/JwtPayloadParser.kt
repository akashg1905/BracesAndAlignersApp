package com.smylo.core.network

import android.util.Base64
import org.json.JSONObject

/**
 * Minimal JWT payload reader (no signature verification).
 * Used when backend returns only access_token and user id lives in "sub".
 */
object JwtPayloadParser {
    fun parseSub(jwt: String): String? {
        val parts = jwt.split(".")
        if (parts.size < 2) return null
        return try {
            val payload = parts[1].padPayloadForBase64Url()
            val payloadJson = String(
                Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            )
            val obj = JSONObject(payloadJson)
            if (!obj.has("sub")) return null
            obj.optString("sub").takeIf { it.isNotEmpty() }
        } catch (_: Exception) {
            null
        }
    }

    private fun String.padPayloadForBase64Url(): String {
        return when (length % 4) {
            2 -> this + "=="
            3 -> this + "="
            else -> this
        }
    }
}

