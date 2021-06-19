package com.sub1_made.core.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sub1_made.core.R

object Helper {

    const val posterLink = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/"
    const val previewLink = "https://image.tmdb.org/t/p/original/"

    fun setImageWithGlide(context: Context, imagePath: String?, imageView: ImageView) {
        Glide.with(context)
            .clear(imageView)
        Glide.with(context)
            .load(imagePath)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
            )
            .into(imageView)
    }
}