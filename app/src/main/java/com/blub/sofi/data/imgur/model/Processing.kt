package com.blub.sofi.data.imgur.model


import com.squareup.moshi.Json

data class Processing(
    @Json(name = "status")
    val status: String
)