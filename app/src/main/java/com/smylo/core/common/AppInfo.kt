package com.smylo.core.common

import com.smylo.BuildConfig

object AppInfo {
    val versionName: String = BuildConfig.VERSION_NAME

    fun versionLabel(prefix: String = "V"): String = "$prefix$versionName"
}
