package com.dicoding.sub1_made.home.tvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.domain.usecase.CatalogUseCase

class TvViewModel(private val catalogUseCase: CatalogUseCase) :
    ViewModel() {

    fun getListTvShows(sort: String): LiveData<Resource<List<TvDomain>>> =
        catalogUseCase.getListTvShows(sort).asLiveData()
}