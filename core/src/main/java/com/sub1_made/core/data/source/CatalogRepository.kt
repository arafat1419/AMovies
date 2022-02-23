package com.sub1_made.core.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.sub1_made.core.data.model.DataTrailer
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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class  CatalogRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ICatalogRepository {

    override fun getSearchMovies(query: String): Flow<List<MovieDomain>> {
        val movieEntity = MutableLiveData<List<MovieEntity>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getSearchMovies(query).collect { response ->
                when (response) {
                    is ApiResponse.Empty -> movieEntity.postValue(listOf())
                    is ApiResponse.Error -> response.errorMessage
                    is ApiResponse.Success -> movieEntity.postValue(
                        DataMapper.movieResponseToEntities(
                            response.data
                        )
                    )
                }
            }
        }
        return movieEntity.map {
            DataMapper.movieEntitiesToDomain(it)
        }.asFlow()
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

    override fun getTrailerMovie(movieId: Int): LiveData<DataTrailer> {
        val movieTrailer = MutableLiveData<DataTrailer>()
        remoteDataSource.getTrailerMovie(movieId, object : RemoteCallback.LoadTrailerMovieCallback {
            var trailer = DataTrailer()
            override fun onTrailerMovieReceived(movieTrailerResponse: List<TrailerResponse>) {
                if (movieTrailerResponse.isNotEmpty())
                    trailer =
                        DataTrailer(movieTrailerResponse[0].link.toString())
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
        CoroutineScope(IO).launch {
            localDataSource.setFavMovie(movieEntity, state)
        }
    }

    override fun getSearchTvShows(query: String): Flow<List<TvDomain>> {
        val tvEntity = MutableLiveData<List<TvEntity>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getSearchTvShows(query).collect { response ->
                when (response) {
                    /*is ApiResponse.Empty ->
                    is ApiResponse.Error -> response.errorMessage*/
                    is ApiResponse.Success -> {
                        tvEntity.postValue(DataMapper.tvResponseToEntities(response.data))
                    }
                }
            }
        }
        return tvEntity.map {
            DataMapper.tvEntitiesToDomain(it)
        }.asFlow()
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

    override fun getTrailerTvShow(tvId: Int): LiveData<DataTrailer> {
        val tvTrailer = MutableLiveData<DataTrailer>()
        remoteDataSource.getTrailerTvShow(tvId, object : RemoteCallback.LoadTrailerTvCallback {
            var trailer = DataTrailer()
            override fun onTrailerTvReceived(tvTrailerResponse: List<TrailerResponse>) {
                if (tvTrailerResponse.isNotEmpty())
                    trailer =
                        DataTrailer(tvTrailerResponse[0].link.toString())
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
        CoroutineScope(IO).launch {
            localDataSource.setFavTvShow(tvEntity, state)
        }
    }
}
