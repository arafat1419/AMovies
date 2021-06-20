package com.sub1_made.core.data.source.remote

import com.sub1_made.core.data.source.remote.api.ApiService
import com.sub1_made.core.data.source.remote.response.ApiResponse
import com.sub1_made.core.data.source.remote.response.MovieResponse
import com.sub1_made.core.data.source.remote.response.TvResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getSearchMovies(query: String): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = apiService.getSearchMovie(query)
                val movieList = response.result
                if (movieList != null) {
                    if (movieList.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(IO)
    }

    fun getSearchTvShows(query: String): Flow<ApiResponse<List<TvResponse>>> {
        return flow {
            try {
                val response = apiService.getSearchTvShow(query)
                val tvList = response.result
                if (tvList != null) {
                    if (tvList.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(IO)
    }

    suspend fun getTopRatedMovies(): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = apiService.getTopRatedMovies()
                val movieList = response.result
                if (movieList != null) {
                    if (movieList.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(IO)
    }

    suspend fun getTopRatedTvShows(): Flow<ApiResponse<List<TvResponse>>> {
        return flow {
            try {
                val response = apiService.getTopRatedTvShow()
                val tvShowList = response.result
                if (tvShowList != null) {
                    if (tvShowList.isNotEmpty()) {
                        emit(ApiResponse.Success(response.result))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(IO)
    }

    fun getTrailerMovie(movieId: Int, callback: RemoteCallback.LoadTrailerMovieCallback) {
        CoroutineScope(IO).launch {
            try {
                apiService.getTrailerMovie(movieId).await().result?.let {
                    callback.onTrailerMovieReceived(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun getTrailerTvShow(tvShowId: Int, callback: RemoteCallback.LoadTrailerTvCallback) {
        CoroutineScope(IO).launch {
            try {
                apiService.getTrailerTv(tvShowId).await().result?.let {
                    callback.onTrailerTvReceived(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    fun getDetailSearchMovies(
        movieId: Int,
        callback: RemoteCallback.LoadDetailSearchMovieCallback
    ) {
        CoroutineScope(IO).launch {
            try {
                apiService.getDetailSearchMovie(movieId).await().let {
                    callback.onDetailSearchMovieReceived(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun getDetailSearchTvShow(tvShowId: Int, callback: RemoteCallback.LoadDetailSearchTvCallback) {
        CoroutineScope(IO).launch {
            try {
                apiService.getDetailSearchTv(tvShowId).await().let {
                    callback.onDetailSearchTvReceived(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}