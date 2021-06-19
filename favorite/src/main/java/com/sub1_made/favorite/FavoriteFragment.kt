package com.sub1_made.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sub1_made.favorite.databinding.FragmentFavoriteBinding
import com.sub1_made.favorite.di.favoriteModule
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadKoinModules(favoriteModule)

        context?.let { setViewPager(it) }
    }

    private fun setViewPager(context: Context) {
        val sectionsPagerAdapter = SectionPagerAdapter(context, childFragmentManager)
        binding?.viewPager?.adapter = sectionsPagerAdapter
        binding?.tabLayout?.setupWithViewPager(binding!!.viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}