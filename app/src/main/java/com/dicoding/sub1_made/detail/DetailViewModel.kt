package com.dicoding.sub1_made.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sub1_made.core.data.model.DataTrailer
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.usecase.CatalogUseCase

class DetailViewModel(private val catalogUseCase: CatalogUseCase) :
    ViewModel() {

    fun getDetailMovie(movieId: Int): LiveData<MovieDomain> =
        catalogUseCase.getDetailMovie(movieId)

    fun getDetailTvShows(tvShowId: Int): LiveData<TvDomain> =
        catalogUseCase.getDetailTvShow(tvShowId)

    fun getTrailerMovie(movieId: Int): LiveData<DataTrailer> =
        catalogUseCase.getTrailerMovie(movieId)

    fun getTrailerTv(tvId: Int): LiveData<DataTrailer> =
        catalogUseCase.getTrailerTvShow(tvId)

    fun getDetailSearchMovie(movieId: Int): LiveData<MovieDomain> =
        catalogUseCase.getDetailSearchMovie(movieId)

    fun getDetailSearchTv(tvId: Int): LiveData<TvDomain> =
        catalogUseCase.getDetailSearchTvShows(tvId)

    fun setFavoriteMovie(movie: MovieDomain, state: Boolean) {
        catalogUseCase.setFavMovie(movie, state)
    }

    fun setFavoriteTvShow(tvShow: TvDomain, state: Boolean) {
        catalogUseCase.setFavTvShow(tvShow, state)
    }
}