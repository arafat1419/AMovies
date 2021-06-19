package com.sub1_made.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TvResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val title: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("poster_path")
    val poster: String? = null,

    @field:SerializedName("backdrop_path")
    val imgPreview: String? = null,

    @field:SerializedName("vote_average")
    val rating: Double,

    @field:SerializedName("first_air_date")
    val releaseDate: String? = null
)
