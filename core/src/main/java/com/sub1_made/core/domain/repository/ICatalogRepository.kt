package com.sub1_made.core.domain.repository

import androidx.lifecycle.LiveData
import com.sub1_made.core.data.model.DataTrailer
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import kotlinx.coroutines.flow.Flow

interface ICatalogRepository {
    fun getSearchMovies(query: String): LiveData<List<MovieDomain>>
    fun getTrailerMovie(movieId: Int): LiveData<DataTrailer>
    fun getDetailSearchMovie(movieId: Int): LiveData<MovieDomain>

    fun getSearchTvShows(query: String): LiveData<List<TvDomain>>
    fun getTrailerTvShow(tvId: Int): LiveData<DataTrailer>
    fun getDetailSearchTvShows(tvShowId: Int): LiveData<TvDomain>

    fun getListMovies(sort: String): Flow<Resource<List<MovieDomain>>>

    fun getDetailMovie(movieId: Int): LiveData<MovieDomain>
    fun getListFavMovies(): Flow<List<MovieDomain>>
    fun setFavMovie(movie: MovieDomain, state: Boolean)

    fun getListTvShows(sort: String): Flow<Resource<List<TvDomain>>>

    fun getDetailTvShow(tvShowId: Int): LiveData<TvDomain>
    fun getListFavTvShows(): Flow<List<TvDomain>>
    fun setFavTvShow(tvShow: TvDomain, state: Boolean)
}