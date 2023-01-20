package com.tauan.themovieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tauan.themovieapp.R
import com.tauan.themovieapp.databinding.FragmentItemBinding
import com.tauan.themovieapp.model.Movie

interface MovieItemListener {
    fun onItemSelected(position: Int)
}

class MovieRecyclerViewAdapter(
    private val listener: MovieItemListener,
) : RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {

    private val values: MutableList<Movie> = ArrayList()

    fun updateData(movieList: List<Movie>) {
        values.clear()
        values.addAll(movieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.fragment_item,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

        Glide.with(holder.root).load(item.image).into(holder.image)

        holder.root.setOnClickListener { listener.onItemSelected(position) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val root: View = binding.root
        val image: AppCompatImageView = binding.movieImage

        fun bind(item: Movie) {
            binding.movie = item
            binding.executePendingBindings()
        }
    }

}