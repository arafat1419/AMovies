package com.dicoding.sub1_made.home.movies

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.sub1_made.R
import com.dicoding.sub1_made.databinding.FragmentMoviesBinding
import com.dicoding.sub1_made.detail.DetailActivity
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.ui.MovieAdapter
import com.sub1_made.core.ui.MovieCallback
import com.sub1_made.core.utils.Sorting.NAME
import com.sub1_made.core.utils.Sorting.RANDOM
import com.sub1_made.core.utils.Sorting.VOTE_BEST
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment(), MovieCallback {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding
    private val viewModel: MoviesViewModel by viewModel()
    private var sort: String = VOTE_BEST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        showLoading(true)

        activity?.let {
            viewModel.getListMovies(VOTE_BEST).observe(viewLifecycleOwner, moviesObserver)
        }

        binding?.searchFab?.setOnClickListener {
            it.visibility = View.GONE
            val mFragment: Fragment = instantiateFragment()!!
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, mFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_best -> sort = VOTE_BEST
            R.id.action_name -> sort = NAME
            R.id.action_random -> sort = RANDOM
        }

        viewModel.getListMovies(sort).observe(viewLifecycleOwner, moviesObserver)
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(data: MovieDomain) {
        Intent(context, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TYPE, "Movies")
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            context?.startActivity(it)
        }
    }

    private val moviesObserver = Observer<Resource<List<MovieDomain>>> { movies ->
        if (movies != null) {
            when (movies) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    binding?.rvMovies?.adapter?.let { adapter ->
                        when (adapter) {
                            is MovieAdapter -> {
                                adapter.setData(movies.data)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    showLoading(true)
                    Toast.makeText(
                        context,
                        "Check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun instantiateFragment(): Fragment? {
        return try {
            Class.forName("com.sub1_made.search.movies.SearchMovieFragment").newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(context, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun showLoading(state: Boolean) {
        binding!!.apply {
            if (state) {
                imgLoading.visibility = View.VISIBLE
            } else {
                imgLoading.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView() {
        binding?.rvMovies?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MovieAdapter(this@MoviesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvMovies?.adapter = null
        _binding = null
    }
}