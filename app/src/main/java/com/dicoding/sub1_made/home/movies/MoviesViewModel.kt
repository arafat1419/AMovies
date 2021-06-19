package com.dicoding.sub1_made.home.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.usecase.CatalogUseCase

class MoviesViewModel(private val catalogUseCase: CatalogUseCase) : ViewModel() {
    fun getListMovies(sort: String): LiveData<Resource<List<MovieDomain>>> =
        catalogUseCase.getListMovies(sort).asLiveData()
}