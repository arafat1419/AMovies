package com.sub1_made.core.domain.usecase

import androidx.lifecycle.LiveData
import com.sub1_made.core.data.model.DataTrailer
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.repository.ICatalogRepository
import kotlinx.coroutines.flow.Flow

class CatalogInteractor(private val iCatalogRepository: ICatalogRepository) : CatalogUseCase {
    override fun getSearchMovies(query: String): LiveData<List<MovieDomain>> =
        iCatalogRepository.getSearchMovies(query)

    override fun getTrailerMovie(movieId: Int): LiveData<DataTrailer> =
        iCatalogRepository.getTrailerMovie(movieId)

    override fun getDetailSearchMovie(movieId: Int): LiveData<MovieDomain> =
        iCatalogRepository.getDetailSearchMovie(movieId)

    override fun getSearchTvShows(query: String): LiveData<List<TvDomain>> =
        iCatalogRepository.getSearchTvShows(query)

    override fun getTrailerTvShow(tvId: Int): LiveData<DataTrailer> =
        iCatalogRepository.getTrailerTvShow(tvId)

    override fun getDetailSearchTvShows(tvShowId: Int): LiveData<TvDomain> =
        iCatalogRepository.getDetailSearchTvShows(tvShowId)

    override fun getListMovies(sort: String): Flow<Resource<List<MovieDomain>>> =
        iCatalogRepository.getListMovies(sort)

    override fun getDetailMovie(movieId: Int): LiveData<MovieDomain> =
        iCatalogRepository.getDetailMovie(movieId)

    override fun getListFavMovies(): Flow<List<MovieDomain>> =
        iCatalogRepository.getListFavMovies()

    override fun setFavMovie(movie: MovieDomain, state: Boolean) =
        iCatalogRepository.setFavMovie(movie, state)

    override fun getListTvShows(sort: String): Flow<Resource<List<TvDomain>>> =
        iCatalogRepository.getListTvShows(sort)

    override fun getDetailTvShow(tvShowId: Int): LiveData<TvDomain> =
        iCatalogRepository.getDetailTvShow(tvShowId)

    override fun getListFavTvShows(): Flow<List<TvDomain>> =
        iCatalogRepository.getListFavTvShows()

    override fun setFavTvShow(tvShow: TvDomain, state: Boolean) =
        iCatalogRepository.setFavTvShow(tvShow, state)
}