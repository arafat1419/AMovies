package com.dicoding.sub1_made.di

import com.dicoding.sub1_made.detail.DetailViewModel
import com.dicoding.sub1_made.home.movies.MoviesViewModel
import com.dicoding.sub1_made.home.tvshows.TvViewModel
import com.sub1_made.core.domain.usecase.CatalogInteractor
import com.sub1_made.core.domain.usecase.CatalogUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<CatalogUseCase> { CatalogInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { DetailViewModel(get()) }
    viewModel { MoviesViewModel(get()) }
    viewModel { TvViewModel(get()) }
}