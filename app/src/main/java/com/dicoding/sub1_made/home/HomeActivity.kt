package com.dicoding.sub1_made.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dicoding.sub1_made.R
import com.dicoding.sub1_made.databinding.ActivityHomeBinding
import com.dicoding.sub1_made.home.movies.MoviesFragment
import com.dicoding.sub1_made.home.tvshows.TvShowsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        setActiveFragment(MoviesFragment(), resources.getString(R.string.tab_title_1))

        binding.bottomNavbar.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> setActiveFragment(
                    MoviesFragment(),
                    resources.getString(R.string.tab_title_1)
                )
                1 -> setActiveFragment(
                    TvShowsFragment(),
                    resources.getString(R.string.tab_title_2)
                )
                2 -> favoriteFragment()
            }
        }
    }

    private fun favoriteFragment() {
        val fragment = instantiateFragment()
        val title = resources.getString(R.string.tab_title_3)
        if (fragment != null) {
            setActiveFragment(fragment, title)
        }
    }

    private fun instantiateFragment(): Fragment? {
        return try {
            Class.forName("com.sub1_made.favorite.FavoriteFragment").newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun setActiveFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }.commit()

        setActionBarTitle(title)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}