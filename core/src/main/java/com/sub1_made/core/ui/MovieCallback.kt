package com.sub1_made.core.ui

import com.sub1_made.core.domain.model.MovieDomain

interface MovieCallback {
    fun onItemClicked(data: MovieDomain)
}