package com.sub1_made.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvDomain(
    val id: Int = 0,
    val title: String? = null,
    val overview: String? = null,
    val poster: String? = null,
    val imgPreview: String? = null,
    val rating: Double = 0.0,
    val releaseDate: String? = null,
    val isFavorite: Boolean = false
) : Parcelable
