package com.sub1_made.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sub1_made.core.databinding.ItemsRowDataBinding
import com.sub1_made.core.domain.model.MovieDomain
import com.sub1_made.core.utils.Helper
import java.util.*

class MovieAdapter(private val callback: MovieCallback) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var listData = ArrayList<MovieDomain>()

    fun setData(newListData: List<MovieDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemsDataBinding =
            ItemsRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ViewHolder(private val binding: ItemsRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieDomain) {
            with(binding) {
                txtTitle.text = data.title
                txtRating.text = data.rating.toString()
                Helper.setImageWithGlide(itemView.context, data.poster, imgPoster)
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}