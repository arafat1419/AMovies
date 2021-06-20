package com.sub1_made.core.data.source.remote

import com.sub1_made.core.data.source.remote.response.MovieResponse
import com.sub1_made.core.data.source.remote.response.TrailerResponse
import com.sub1_made.core.data.source.remote.response.TvResponse

object RemoteCallback {

    interface LoadTrailerMovieCallback {
        fun onTrailerMovieReceived(movieTrailerResponse: List<TrailerResponse>)
    }

    interface LoadTrailerTvCallback {
        fun onTrailerTvReceived(tvTrailerResponse: List<TrailerResponse>)
    }

    interface LoadDetailSearchMovieCallback {
        fun onDetailSearchMovieReceived(movieResponse: MovieResponse)
    }

    interface LoadDetailSearchTvCallback {
        fun onDetailSearchTvReceived(tvResponse: TvResponse)
    }
}