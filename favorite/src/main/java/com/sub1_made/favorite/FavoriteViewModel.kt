package com.sub1_made.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.usecase.CatalogUseCase

class FavoriteViewModel(private val catalogUseCase: CatalogUseCase) :
    ViewModel() {

    fun getListFavMovies(): LiveData<List<MovieDomain>> =
        catalogUseCase.getListFavMovies().asLiveData()

    fun getListFavTvShows(): LiveData<List<TvDomain>> =
        catalogUseCase.getListFavTvShows().asLiveData()
}