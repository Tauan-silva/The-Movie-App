package com.tauan.themovieapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.tauan.themovieapp.R
import com.tauan.themovieapp.databinding.FragmentMovieDetailsBinding
import com.tauan.themovieapp.ui.viewmodel.MovieViewModel

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel by navGraphViewModels<MovieViewModel>(R.id.app_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_movie_details,
                container,
                false
            )

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        initObservers()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.postersLiveData.observe(viewLifecycleOwner) { posterList ->
            if (posterList == null) {
                binding.labelPosters.visibility = View.GONE
                binding.postersList.visibility = View.GONE
                return@observe
            }

            with(binding.postersList) {
                registerLifecycle(lifecycle)
                imageScaleType = ImageView.ScaleType.CENTER_CROP
                setData(posterList)
            }
        }

        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(this)
                    .load(it.image)
                    .into(binding.movieImageDetails)

                binding.movieName.text = "${it.title} (${it.dateRelease})"
                viewModel.getMoviePosters(it)
            }
        }
    }

}