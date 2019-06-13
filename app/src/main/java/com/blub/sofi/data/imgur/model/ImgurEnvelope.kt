package com.blub.sofi.data.imgur.model


import com.squareup.moshi.Json

data class ImgurEnvelope(
    @Json(name = "data")
    val `data`: List<ImageResource>,
    @Json(name = "status")
    val status: Int,
    @Json(name = "success")
    val success: Boolean
)