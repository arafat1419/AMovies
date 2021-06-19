package com.sub1_made.favorite.tvShows

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.sub1_made.detail.DetailActivity
import com.sub1_made.core.domain.model.TvDomain
import com.sub1_made.core.ui.TvAdapter
import com.sub1_made.core.ui.TvCallback
import com.sub1_made.favorite.FavoriteViewModel
import com.sub1_made.favorite.databinding.FragmentFavTvShowsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class FavTvShowsFragment : Fragment(), TvCallback {

    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavTvShowsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavTvShowsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()

        parentFragment?.let {
            viewModel.getListFavTvShows().observe(viewLifecycleOwner, { tv ->
                if (tv != null) {
                    binding?.rvFavTv?.adapter?.let { adapter ->
                        when (adapter) {
                            is TvAdapter -> {
                                if (tv.isNullOrEmpty()) {
                                    emptyData(true)
                                } else {
                                    adapter.setData(tv)
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
                rvFavTv.visibility = View.GONE
                imgEmpty.visibility = View.VISIBLE
            } else {
                rvFavTv.visibility = View.VISIBLE
                imgEmpty.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView() {
        binding?.rvFavTv?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = TvAdapter(this@FavTvShowsFragment)
        }
    }

    override fun onItemClicked(data: TvDomain) {
        Intent(context, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TYPE, "TvShows")
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            context?.startActivity(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.rvFavTv?.adapter = null
        _binding = null
    }
}