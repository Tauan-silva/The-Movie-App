package com.tauan.themovieapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tauan.themovieapp.R
import com.tauan.themovieapp.databinding.FragmentMovieListBinding
import com.tauan.themovieapp.ui.adapter.MovieItemListener
import com.tauan.themovieapp.ui.adapter.MovieRecyclerViewAdapter
import com.tauan.themovieapp.ui.viewmodel.MovieViewModel
import com.tauan.themovieapp.util.DataState

class MovieListFragment : Fragment(), MovieItemListener {

    private val viewModel by navGraphViewModels<MovieViewModel>(R.id.app_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var adapter: MovieRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_movie_list, container, false
        )
        adapter = MovieRecyclerViewAdapter(this@MovieListFragment)

        setMenu()

        // Set the adapter
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MovieListFragment.adapter
        }

        initObservers()

        return binding.root
    }

    override fun onItemSelected(position: Int) {
        viewModel.onMovieSelected(position)
    }

    private fun initObservers() {
        viewModel.listLiveData.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.updateData(list)
            } else {
                Snackbar.make(
                    binding.root, "Something doesn't worked :(", Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.navigationToDetailsLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                val action =
                    MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DataState.LOADING -> {
                    binding.list.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                DataState.ERROR -> {
                    Snackbar.make(
                        binding.root, "Something doesn't worked :(", Snackbar.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.list.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setMenu() {
        val menuNavHost: MenuHost = requireActivity()

        menuNavHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}