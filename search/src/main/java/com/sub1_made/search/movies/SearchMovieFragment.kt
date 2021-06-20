package com.sub1_made.search.movies

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.sub1_made.detail.DetailActivity
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.ui.MovieAdapter
import com.sub1_made.core.ui.MovieCallback
import com.sub1_made.search.R
import com.sub1_made.search.SearchViewModel
import com.sub1_made.search.databinding.FragmentSearchBinding
import com.sub1_made.search.di.searchModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class SearchMovieFragment : Fragment(), MovieCallback {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private lateinit var searchView: SearchView
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(searchModule)

        setSearch()
        setRecyclerView()
        setHasOptionsMenu(true)
    }

    override fun onItemClicked(data: MovieDomain) {
        Intent(context, Class.forName("com.dicoding.sub1_made.detail.DetailActivity")).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TYPE, "Movies")
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            it.putExtra(DetailActivity.EXTRA_SEARCH, true)
            context?.startActivity(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                binding?.arc?.visibility = View.GONE
                CoroutineScope(IO).launch {
                    viewModel.queryChannel.send(query)
                }
                showLoading(true)
                return true
            }
        })
    }

    private fun setSearch() {
        viewModel.movieResult.observe(viewLifecycleOwner, searchObserver)
    }

    private val searchObserver = Observer<List<MovieDomain>> {
        if (it.isNullOrEmpty()) {
            emptyData(true)
        } else {
            emptyData(false)
        }
        setRecyclerView()
        showLoading(false)
        binding?.rvSearch?.adapter.let { adapter ->
            when (adapter) {
                is MovieAdapter -> {
                    adapter.setData(it)
                }
            }
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

    private fun emptyData(state: Boolean) {
        binding!!.apply {
            if (state) {
                imgEmpty.visibility = View.VISIBLE
            } else {
                imgEmpty.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView() {
        binding?.rvSearch?.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = MovieAdapter(this@SearchMovieFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvSearch?.adapter = null
        _binding = null
    }
}