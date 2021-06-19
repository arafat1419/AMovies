package com.sub1_made.favorite.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.sub1_made.detail.DetailActivity
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.ui.MovieAdapter
import com.sub1_made.core.ui.MovieCallback
import com.sub1_made.favorite.FavoriteViewModel
import com.sub1_made.favorite.databinding.FragmentFavMoviesBinding
import org.koin.android.viewmodel.ext.android.viewModel

class FavMoviesFragment : Fragment(), MovieCallback {

    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavMoviesBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavMoviesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()

        parentFragment?.let {
            viewModel.getListFavMovies().observe(viewLifecycleOwner, { movie ->
                if (movie != null) {
                    binding?.rvFavMovies?.adapter?.let { adapter ->
                        when (adapter) {
                            is MovieAdapter -> {
                                if (movie.isNullOrEmpty()) {
                                    emptyData(true)
                                } else {
                                    adapter.setData(movie)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun emptyData(state: Boolean) {
        with(binding!!) {
            if (state) {
                rvFavMovies.visibility = View.GONE
                imgEmpty.visibility = View.VISIBLE
            } else {
                rvFavMovies.visibility = View.VISIBLE
                imgEmpty.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView() {
        binding?.rvFavMovies?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MovieAdapter(this@FavMoviesFragment)
        }
    }

    override fun onItemClicked(data: MovieDomain) {
        Intent(context, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TYPE, "Movies")
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            context?.startActivity(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvFavMovies?.adapter = null
    }
}