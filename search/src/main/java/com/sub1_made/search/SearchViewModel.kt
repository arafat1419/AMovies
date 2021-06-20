package com.sub1_made.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sub1_made.core.domain.usecase.CatalogUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val catalogUseCase: CatalogUseCase) : ViewModel() {
    val queryChannel = ConflatedBroadcastChannel<String>()

    val movieResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            catalogUseCase.getSearchMovies(it)
        }.asLiveData()

    val tvResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            catalogUseCase.getSearchTvShows(it)
        }.asLiveData()
}