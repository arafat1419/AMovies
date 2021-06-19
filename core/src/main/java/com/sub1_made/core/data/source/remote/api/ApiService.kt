package com.sub1_made.core.data.source.remote.api

import com.sub1_made.core.BuildConfig
import com.sub1_made.core.data.source.remote.response.ListResponse
import com.sub1_made.core.data.source.remote.response.MovieResponse
import com.sub1_made.core.data.source.remote.response.TrailerResponse
import com.sub1_made.core.data.source.remote.response.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    fun getSearchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<ListResponse<MovieResponse>>

    @GET("search/tv")
    fun getSearchTvShow(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<ListResponse<TvResponse>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): ListResponse<MovieResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShow(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): ListResponse<TvResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailerMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<ListResponse<TrailerResponse>>

    @GET("tv/{tv_id}/videos")
    fun getTrailerTv(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<ListResponse<TrailerResponse>>

    @GET("movie/{movie_id}")
    fun getDetailSearchMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<MovieResponse>

    @GET("tv/{tv_id}")
    fun getDetailSearchTv(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<TvResponse>
}