package com.sub1_made.core.ui

import com.sub1_made.core.domain.model.TvDomain

interface TvCallback {
    fun onItemClicked(data: TvDomain)
}