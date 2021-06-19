package com.sub1_made.favorite

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sub1_made.favorite.movies.FavMoviesFragment
import com.sub1_made.favorite.tvShows.FavTvShowsFragment

class SectionPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_title_movies, R.string.tab_title_tv)
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> FavMoviesFragment()
            1 -> FavTvShowsFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence = mContext.resources.getString(
        TAB_TITLES[position]
    )

    override fun getCount(): Int = 2

}