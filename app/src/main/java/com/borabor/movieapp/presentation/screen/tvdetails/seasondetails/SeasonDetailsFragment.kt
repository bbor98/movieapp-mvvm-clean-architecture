package com.borabor.movieapp.presentation.screen.tvdetails.seasondetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentSeasonDetailsBinding
import com.borabor.movieapp.presentation.adapter.EpisodeAdapter
import com.borabor.movieapp.presentation.adapter.ImageAdapter
import com.borabor.movieapp.presentation.adapter.PersonAdapter
import com.borabor.movieapp.presentation.adapter.VideoAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.playYouTubeVideo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonDetailsFragment : BaseFragment<FragmentSeasonDetailsBinding>(R.layout.fragment_season_details) {

    override val viewModel: SeasonDetailsViewModel by viewModels()

    val adapterVideos = VideoAdapter { playYouTubeVideo(it) }
    val adapterCast = PersonAdapter(isCast = true)
    val adapterEpisodes by lazy { EpisodeAdapter(detailId, backgroundColor) }
    val adapterImages = ImageAdapter(isPortrait = true)

    private val seasonNumber by lazy { arguments?.getInt(Constants.SEASON_NUMBER)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageRecyclerViewAdapterLifecycle(
            binding.rvVideos,
            binding.rvCast,
            binding.rvEpisodes,
            binding.rvImages,
        )

        viewModel.initRequest(detailId, seasonNumber)

        collectFlows(
            listOf(
                ::collectSeasonDetails,
                ::collectUiState
            )
        )
    }

    private suspend fun collectSeasonDetails() {
        viewModel.details.collect { seasonDetails ->
            adapterCast.submitList(seasonDetails.credits.cast)
            adapterVideos.submitList(seasonDetails.videos.filterVideos())
            adapterEpisodes.submitList(seasonDetails.episodes)
            adapterImages.submitList(seasonDetails.images.posters)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(message = state.errorText!!, actionText = getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequest(id, seasonNumber)
                }
            }
        }
    }
}