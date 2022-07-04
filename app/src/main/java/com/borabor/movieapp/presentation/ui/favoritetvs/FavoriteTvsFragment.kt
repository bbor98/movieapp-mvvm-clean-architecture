package com.borabor.movieapp.presentation.ui.favoritetvs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentFavoriteTvsBinding
import com.borabor.movieapp.domain.model.FavoriteTv
import com.borabor.movieapp.presentation.adapter.FavoriteTvAdapter
import com.borabor.movieapp.presentation.ui.base.BaseFragment
import com.borabor.movieapp.util.LifecycleRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteTvsFragment : BaseFragment<FragmentFavoriteTvsBinding>(R.layout.fragment_favorite_tvs) {

    private val viewModel: FavoriteTvsViewModel by activityViewModels()

    override val defineBindingVariables: (FragmentFavoriteTvsBinding) -> Unit
        get() = { binding ->
            binding.fragment = this
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
        }

    val adapterFavorites = FavoriteTvAdapter { removeTv(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleRecyclerView(binding.recyclerView))
        collectFlows(listOf(::collectFavoriteTvs))
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFavoriteTvs()
    }

    private fun removeTv(tv: FavoriteTv) {
        viewModel.removeTvFromFavorites(tv)

        showSnackbar(
            message = getString(R.string.snackbar_removed_item),
            actionText = getString(R.string.snackbar_action_undo),
            anchor = true
        ) {
            viewModel.addTvToFavorites(tv)
        }
    }

    private suspend fun collectFavoriteTvs() {
        viewModel.favoriteTvs.collect { favoriteTvs ->
            adapterFavorites.submitList(favoriteTvs)
        }
    }
}