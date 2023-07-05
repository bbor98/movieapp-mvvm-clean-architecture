package com.borabor.movieapp.presentation.screen.tvdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentTvDetailsBinding
import com.borabor.movieapp.presentation.adapter.ImageAdapter
import com.borabor.movieapp.presentation.adapter.PersonAdapter
import com.borabor.movieapp.presentation.adapter.SeasonAdapter
import com.borabor.movieapp.presentation.adapter.TvAdapter
import com.borabor.movieapp.presentation.adapter.VideoAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.util.Detail
import com.borabor.movieapp.util.playYouTubeVideo
import com.borabor.movieapp.util.setGenreChips
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvDetailsFragment : BaseFragment<FragmentTvDetailsBinding>(R.layout.fragment_tv_details) {

    override val viewModel: TvDetailsViewModel by viewModels()

    val adapterVideos = VideoAdapter { playYouTubeVideo(it) }
    val adapterCast = PersonAdapter(isCast = true)
    val adapterImages = ImageAdapter()
    val adapterRecommendations = TvAdapter()
    val adapterSeasons by lazy { SeasonAdapter(detailId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageRecyclerViewAdapterLifecycle(
            binding.rvVideos,
            binding.rvCast,
            binding.rvImages,
            binding.rvSeasons,
            binding.rvRecommendations
        )

        viewModel.initRequest(detailId)

        collectFlows(
            listOf(
                ::collectDetails,
                ::collectUiState
            )
        )
    }

    private suspend fun collectDetails() {
        viewModel.details.collect { details ->
            binding.cgGenres.setGenreChips(details.genres, Detail.TV, backgroundColor)
            adapterVideos.submitList(details.videos.filterVideos())
            adapterCast.submitList(details.credits.cast)
            adapterImages.submitList(details.images.backdrops)
            adapterSeasons.submitList(details.seasons)
            adapterRecommendations.submitList(details.recommendations.results)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(message = state.errorText!!, actionText = getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequest(id)
                }
            }
        }
    }
}