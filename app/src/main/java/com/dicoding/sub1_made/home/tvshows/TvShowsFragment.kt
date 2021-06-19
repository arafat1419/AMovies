package com.dicoding.sub1_made.home.tvshows

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.sub1_made.R
import com.dicoding.sub1_made.databinding.FragmentTvShowsBinding
import com.dicoding.sub1_made.detail.DetailActivity
import com.sub1_made.core.data.source.Resource
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.ui.TvAdapter
import com.sub1_made.core.ui.TvCallback
import com.sub1_made.core.utils.Sorting
import com.sub1_made.core.utils.Sorting.VOTE_BEST
import org.koin.android.viewmodel.ext.android.viewModel

class TvShowsFragment : Fragment(), TvCallback {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding
    private val viewModel: TvViewModel by viewModel()
    private var sort: String = VOTE_BEST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        showLoading(true)

        activity?.let {
            viewModel.getListTvShows(VOTE_BEST).observe(viewLifecycleOwner, tvShowsObeserver)
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
            R.id.action_name -> sort = Sorting.NAME
            R.id.action_random -> sort = Sorting.RANDOM
        }

        viewModel.getListTvShows(sort).observe(viewLifecycleOwner, tvShowsObeserver)
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(data: TvDomain) {
        Intent(context, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TYPE, "TvShows")
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            context?.startActivity(it)
        }
    }

    private val tvShowsObeserver = Observer<Resource<List<TvDomain>>> { tvShows ->
        if (tvShows != null) {
            when (tvShows) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {

                    showLoading(false)
                    binding?.rvTvShows?.adapter?.let { adapter ->
                        when (adapter) {
                            is TvAdapter -> {
                                adapter.setData(tvShows.data)
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
            Class.forName("com.sub1_made.search.tvshows.SearchTvShowFragment").newInstance() as Fragment
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
        binding?.rvTvShows?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = TvAdapter(this@TvShowsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvTvShows?.adapter = null
        _binding = null
    }
}