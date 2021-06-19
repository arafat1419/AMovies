package com.sub1_made.core.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.sub1_made.core.data.source.local.LocalDataSource
import com.sub1_made.core.data.source.local.entity.MovieEntity
import com.sub1_made.core.data.source.local.entity.TvEntity
import com.sub1_made.core.data.source.remote.RemoteCallback
import com.sub1_made.core.data.source.remote.RemoteDataSource
import com.sub1_made.core.data.source.remote.response.ApiResponse
import com.sub1_made.core.data.source.remote.response.MovieResponse
import com.sub1_made.core.data.source.remote.response.TrailerResponse
import com.sub1_made.core.data.source.remote.response.TvResponse
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.repository.ICatalogRepository
import com.sub1_made.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CatalogRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ICatalogRepository {

    override fun getSearchMovies(query: String): LiveData<List<MovieDomain>> {
        val searchList = MutableLiveData<List<MovieEntity>>()
        remoteDataSource.getSearchMovies(query, object : RemoteCallback.LoadSearchMovieCallback {
            override fun onSearchMovieReceived(searchMovieResponse: List<MovieResponse>) {
                val movieList = ArrayList<MovieEntity>()
                for (i in searchMovieResponse) {
                    val movie = MovieEntity(
                        i.id,
                        i.title,
                        i.overview,
                        i.poster,
                        i.imgPreview,
                        i.rating,
                        i.releaseDate,
                        false
                    )
                    movieList.add(movie)
                }
                searchList.postValue(movieList)
            }
        })
        return searchList.map {
            DataMapper.movieEntitiesToDomain(it)
        }
    }

    override fun getListMovies(sort: String): Flow<Resource<List<MovieDomain>>> {
        return object :
            NetworkBoundResource<List<MovieDomain>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<MovieDomain>> {
                return localDataSource.getListMovies(sort).map {
                    DataMapper.movieEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<MovieDomain>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getTopRatedMovies()

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.movieResponseToEntities(data)
                localDataSource.insertMovies(movieList)
            }

        }.asFlow()
    }


    override fun getDetailMovie(movieId: Int): LiveData<MovieDomain> =
        localDataSource.getDetailMovie(movieId).map {
            DataMapper.movieEntityToDomain(it)
        }

    override fun getDetailSearchMovie(movieId: Int): LiveData<MovieDomain> {
        val movieResult = MutableLiveData<MovieEntity>()
        remoteDataSource.getDetailSearchMovies(
            movieId,
            object : RemoteCallback.LoadDetailSearchMovieCallback {
                override fun onDetailSearchMovieReceived(movieResponse: MovieResponse) {
                    val movie = MovieEntity(
                        movieResponse.id,
                        movieResponse.title,
                        movieResponse.overview,
                        movieResponse.poster,
                        movieResponse.imgPreview,
                        movieResponse.rating,
                        movieResponse.releaseDate,
                        false
                    )
                    movieResult.postValue(movie)
                }
            })
        return movieResult.map {
            DataMapper.movieEntityToDomain(it)
        }
    }

    override fun getTrailerMovie(movieId: Int): LiveData<com.sub1_made.core.data.model.DataTrailer> {
        val movieTrailer = MutableLiveData<com.sub1_made.core.data.model.DataTrailer>()
        remoteDataSource.getTrailerMovie(movieId, object : RemoteCallback.LoadTrailerMovieCallback {
            var trailer = com.sub1_made.core.data.model.DataTrailer()
            override fun onTrailerMovieReceived(movieTrailerResponse: List<TrailerResponse>) {
                if (movieTrailerResponse.isNotEmpty())
                    trailer =
                        com.sub1_made.core.data.model.DataTrailer(movieTrailerResponse[0].link.toString())
                movieTrailer.postValue(trailer)
            }
        })
        return movieTrailer
    }

    override fun getListFavMovies(): Flow<List<MovieDomain>> =
        localDataSource.getListFavMovies().map {
            DataMapper.movieEntitiesToDomain(it)
        }

    override fun setFavMovie(movie: MovieDomain, state: Boolean) {
        val movieEntity = DataMapper.domainToMovieEntity(movie)
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.setFavMovie(movieEntity, state)
        }
    }

    override fun getSearchTvShows(query: String): LiveData<List<TvDomain>> {
        val searchList = MutableLiveData<List<TvEntity>>()
        remoteDataSource.getSearchTvShows(query, object : RemoteCallback.LoadSearchTvCallback {
            override fun onSearchTvReceived(searchTvResponse: List<TvResponse>) {
                val tvList = ArrayList<TvEntity>()
                for (i in searchTvResponse) {
                    val movie = TvEntity(
                        i.id,
                        i.title,
                        i.overview,
                        i.poster,
                        i.imgPreview,
                        i.rating,
                        i.releaseDate,
                        false
                    )
                    tvList.add(movie)
                }
                searchList.postValue(tvList)
            }
        })
        return searchList.map {
            DataMapper.tvEntitiesToDomain(it)
        }
    }

    override fun getListTvShows(sort: String): Flow<Resource<List<TvDomain>>> =
        object :
            NetworkBoundResource<List<TvDomain>, List<TvResponse>>() {
            override fun loadFromDB(): Flow<List<TvDomain>> {
                return localDataSource.getListTvShows(sort).map {
                    DataMapper.tvEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<TvDomain>?): Boolean =
                data == null || data.isEmpty()


            override suspend fun createCall(): Flow<ApiResponse<List<TvResponse>>> =
                remoteDataSource.getTopRatedTvShows()

            override suspend fun saveCallResult(data: List<TvResponse>) {
                val tvList = DataMapper.tvResponseToEntities(data)
                localDataSource.insertTvShows(tvList)
            }

        }.asFlow()

    override fun getDetailTvShow(tvShowId: Int): LiveData<TvDomain> =
        localDataSource.getDetailTvShow(tvShowId).map {
            DataMapper.tvEntityToDomain(it)
        }

    override fun getDetailSearchTvShows(tvShowId: Int): LiveData<TvDomain> {
        val tvResult = MutableLiveData<TvEntity>()
        remoteDataSource.getDetailSearchTvShow(
            tvShowId,
            object : RemoteCallback.LoadDetailSearchTvCallback {
                override fun onDetailSearchTvReceived(tvResponse: TvResponse) {
                    val tv = TvEntity(
                        tvResponse.id,
                        tvResponse.title,
                        tvResponse.overview,
                        tvResponse.poster,
                        tvResponse.imgPreview,
                        tvResponse.rating,
                        tvResponse.releaseDate,
                        false
                    )
                    tvResult.postValue(tv)
                }
            })
        return tvResult.map {
            DataMapper.tvEntityToDomain(it)
        }
    }

    override fun getTrailerTvShow(tvId: Int): LiveData<com.sub1_made.core.data.model.DataTrailer> {
        val tvTrailer = MutableLiveData<com.sub1_made.core.data.model.DataTrailer>()
        remoteDataSource.getTrailerTvShow(tvId, object : RemoteCallback.LoadTrailerTvCallback {
            var trailer = com.sub1_made.core.data.model.DataTrailer()
            override fun onTrailerTvReceived(tvTrailerResponse: List<TrailerResponse>) {
                if (tvTrailerResponse.isNotEmpty())
                    trailer =
                        com.sub1_made.core.data.model.DataTrailer(tvTrailerResponse[0].link.toString())
                tvTrailer.postValue(trailer)
            }
        })
        return tvTrailer
    }

    override fun getListFavTvShows(): Flow<List<TvDomain>> =
        localDataSource.getListFavTvShows().map {
            DataMapper.tvEntitiesToDomain(it)
        }

    override fun setFavTvShow(tvShow: TvDomain, state: Boolean) {
        val tvEntity = DataMapper.domainToTvEntity(tvShow)
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.setFavTvShow(tvEntity, state)
        }
    }
}
