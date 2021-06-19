package com.sub1_made.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.usecase.CatalogUseCase

class SearchViewModel(private val catalogUseCase: CatalogUseCase): ViewModel() {
    fun getSearchMovies(query: String): LiveData<List<MovieDomain>> =
        catalogUseCase.getSearchMovies(query)

    fun getSearchTvShows(query: String): LiveData<List<TvDomain>> =
        catalogUseCase.getSearchTvShows(query)
}