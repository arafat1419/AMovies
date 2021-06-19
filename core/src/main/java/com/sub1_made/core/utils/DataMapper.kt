package com.sub1_made.core.utils

import com.sub1_made.core.data.source.local.entity.MovieEntity
import com.sub1_made.core.data.source.local.entity.TvEntity
import com.sub1_made.core.data.source.remote.response.MovieResponse
import com.sub1_made.core.data.source.remote.response.TvResponse
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.utils.Helper.posterLink
import com.sub1_made.core.utils.Helper.previewLink


object DataMapper {
    fun movieResponseToEntities(input: List<MovieResponse>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()
        input.map {
            val movie = MovieEntity(
                it.id,
                it.title,
                it.overview,
                it.poster,
                it.imgPreview,
                it.rating,
                it.releaseDate,
                false
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun tvResponseToEntities(input: List<TvResponse>): List<TvEntity> {
        val tvList = ArrayList<TvEntity>()
        input.map {
            val tv = TvEntity(
                it.id,
                it.title,
                it.overview,
                it.poster,
                it.imgPreview,
                it.rating,
                it.releaseDate,
                false
            )
            tvList.add(tv)
        }
        return tvList
    }

    fun movieEntitiesToDomain(input: List<MovieEntity>): List<MovieDomain> {
        return input.map {
            MovieDomain(
                it.id,
                it.title,
                it.overview,
                posterLink + it.poster,
                previewLink + it.imgPreview,
                it.rating,
                it.releaseDate,
                it.isFavorite
            )
        }
    }

    fun tvEntitiesToDomain(input: List<TvEntity>): List<TvDomain> {
        return input.map {
            TvDomain(
                it.id,
                it.title,
                it.overview,
                posterLink + it.poster,
                previewLink + it.imgPreview,
                it.rating,
                it.releaseDate,
                it.isFavorite
            )
        }
    }

    fun domainToMovieEntity(input: MovieDomain): MovieEntity {
        return MovieEntity(
            input.id,
            input.title,
            input.overview,
            input.poster,
            input.imgPreview,
            input.rating,
            input.releaseDate,
            input.isFavorite
        )
    }

    fun domainToTvEntity(input: TvDomain): TvEntity {
        return TvEntity(
            input.id,
            input.title,
            input.overview,
            input.poster,
            input.imgPreview,
            input.rating,
            input.releaseDate,
            input.isFavorite
        )
    }

    fun movieEntityToDomain(input: MovieEntity): MovieDomain {
        return MovieDomain(
            input.id,
            input.title,
            input.overview,
            posterLink + input.poster,
            previewLink + input.imgPreview,
            input.rating,
            input.releaseDate,
            input.isFavorite
        )
    }

    fun tvEntityToDomain(input: TvEntity): TvDomain {
        return TvDomain(
            input.id,
            input.title,
            input.overview,
            posterLink + input.poster,
            previewLink + input.imgPreview,
            input.rating,
            input.releaseDate,
            input.isFavorite
        )
    }
}