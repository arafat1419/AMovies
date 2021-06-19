package com.sub1_made.core.data.source.local

import androidx.lifecycle.LiveData
import com.sub1_made.core.data.source.local.entity.MovieEntity
import com.sub1_made.core.data.source.local.entity.TvEntity
import com.sub1_made.core.data.source.local.room.CatalogDao
import com.sub1_made.core.utils.Sorting
import com.sub1_made.core.utils.Sorting.MOVIE_ENTITIES
import com.sub1_made.core.utils.Sorting.TV_SHOW_ENTITIES
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val catalogDao: CatalogDao) {

    fun getListMovies(sort: String): Flow<List<MovieEntity>> =
        catalogDao.getListMovies(Sorting.getSortedQuery(sort, MOVIE_ENTITIES))

    fun getListFavMovies(): Flow<List<MovieEntity>> = catalogDao.getListFavMovies()

    fun getListTvShows(sort: String): Flow<List<TvEntity>> =
        catalogDao.getListTvShows(Sorting.getSortedQuery(sort, TV_SHOW_ENTITIES))

    fun getListFavTvShows(): Flow<List<TvEntity>> = catalogDao.getListFavTvShows()

    fun getDetailMovie(movieId: Int): LiveData<MovieEntity> = catalogDao.getDetailMovieById(movieId)

    fun getDetailTvShow(tvShowId: Int): LiveData<TvEntity> =
        catalogDao.getDetailTvShowById(tvShowId)

    suspend fun insertMovies(movies: List<MovieEntity>) = catalogDao.insertMovies(movies)

    suspend fun insertTvShows(tvShows: List<TvEntity>) = catalogDao.insertTvShows(tvShows)

    fun setFavMovie(movie: MovieEntity, newState: Boolean) {
        movie.isFavorite = newState
        catalogDao.updateMovie(movie)
    }

    fun setFavTvShow(tvShow: TvEntity, newState: Boolean) {
        tvShow.isFavorite = newState
        catalogDao.updateTvShow(tvShow)
    }
}