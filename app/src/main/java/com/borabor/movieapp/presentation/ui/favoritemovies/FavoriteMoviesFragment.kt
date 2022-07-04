package com.borabor.movieapp.presentation.ui.favoritemovies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentFavoriteMoviesBinding
import com.borabor.movieapp.domain.model.FavoriteMovie
import com.borabor.movieapp.presentation.adapter.FavoriteMovieAdapter
import com.borabor.movieapp.presentation.ui.base.BaseFragment
import com.borabor.movieapp.util.LifecycleRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : BaseFragment<FragmentFavoriteMoviesBinding>(R.layout.fragment_favorite_movies) {

    private val viewModel: FavoriteMoviesViewModel by activityViewModels()

    override val defineBindingVariables: (FragmentFavoriteMoviesBinding) -> Unit
        get() = { binding ->
            binding.fragment = this
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
        }

    val adapterFavorites = FavoriteMovieAdapter { removeMovie(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleRecyclerView(binding.recyclerView))
        collectFlows(listOf(::collectFavoriteMovies))
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFavoriteMovies()
    }

    private fun removeMovie(movie: FavoriteMovie) {
        viewModel.removeMovieFromFavorites(movie)
        showSnackbar(
            message = getString(R.string.snackbar_removed_item),
            actionText = getString(R.string.snackbar_action_undo),
            anchor = true
        ) {
            viewModel.addMovieToFavorites(movie)
        }
    }

    private suspend fun collectFavoriteMovies() {
        viewModel.favoriteMovies.collect { favoriteMovies ->
            adapterFavorites.submitList(favoriteMovies)
        }
    }
}