package com.sub1_made.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("poster_path")
    val poster: String? = null,

    @field:SerializedName("backdrop_path")
    val imgPreview: String? = null,

    @field:SerializedName("vote_average")
    val rating: Double = 0.0,

    @field:SerializedName("release_date")
    val releaseDate: String? = null
)