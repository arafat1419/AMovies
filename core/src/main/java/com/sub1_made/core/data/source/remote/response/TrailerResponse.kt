package com.sub1_made.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    @field:SerializedName("key")
    val link: String? = null
)