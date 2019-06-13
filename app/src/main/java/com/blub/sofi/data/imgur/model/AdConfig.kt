package com.blub.sofi.data.imgur.model


import com.squareup.moshi.Json

data class AdConfig(
    @Json(name = "highRiskFlags")
    val highRiskFlags: List<Any>,
    @Json(name = "safeFlags")
    val safeFlags: List<String>,
    @Json(name = "showsAds")
    val showsAds: Boolean,
    @Json(name = "unsafeFlags")
    val unsafeFlags: List<String>
)