package com.borabor.movieapp.presentation.ui.seasondetails

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ActivitySeasonDetailsBinding
import com.borabor.movieapp.databinding.LayoutBottomSheetBinding
import com.borabor.movieapp.domain.model.Episode
import com.borabor.movieapp.presentation.adapter.EpisodeAdapter
import com.borabor.movieapp.presentation.adapter.ImageAdapter
import com.borabor.movieapp.presentation.adapter.PersonAdapter
import com.borabor.movieapp.presentation.adapter.VideoAdapter
import com.borabor.movieapp.presentation.ui.base.BaseActivity
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.playYouTubeVideo
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonDetailsActivity : BaseActivity<ActivitySeasonDetailsBinding>(R.layout.activity_season_details) {

    private val viewModel: SeasonDetailsViewModel by viewModels()

    override val defineBindingVariables: (ActivitySeasonDetailsBinding) -> Unit
        get() = { binding ->
            binding.activity = this
            binding.lifecycleOwner = this
            binding.videos
            binding.viewModel = viewModel
        }

    val adapterSeasonVideos = VideoAdapter { playYouTubeVideo(it) }
    val adapterSeasonCast = PersonAdapter(isCast = true)
    val adapterSeasonImages = ImageAdapter(isPortrait = true)

    val adapterEpisodes by lazy { EpisodeAdapter(backgroundColor) { showEpisodeDetails(it) } }
    val adapterEpisodeVideos by lazy { VideoAdapter { playYouTubeVideo(it) } }
    val adapterEpisodeCast by lazy { PersonAdapter(isCast = true) }
    val adapterEpisodeGuestStars by lazy { PersonAdapter(isCast = true) }
    val adapterEpisodeImages by lazy { ImageAdapter() }

    private var seasonNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        seasonNumber = intent.getIntExtra(Constants.SEASON_NUMBER, 0)
        viewModel.initRequest(id, seasonNumber)

        collectFlows(listOf(::collectSeasonDetails, ::collectEpisodeDetails, ::collectUiState))
    }

    private fun showEpisodeDetails(episode: Episode) {
        adapterEpisodeVideos.submitList(null)
        adapterEpisodeCast.submitList(null)
        adapterEpisodeGuestStars.submitList(null)
        adapterEpisodeImages.submitList(null)

        viewModel.clearEpisodeData()
        viewModel.fetchEpisodeDetails(seasonNumber, episode.episodeNumber)

        setupBottomSheetDialog(episode)
    }

    private fun setupBottomSheetDialog(episodeItem: Episode) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.ThemeOverlay_Material3_BottomSheetDialog)

        val layoutId = R.layout.layout_bottom_sheet
        val parent = findViewById<NestedScrollView>(R.id.bottomSheetContainer)

        val binding = DataBindingUtil.inflate<LayoutBottomSheetBinding>(LayoutInflater.from(this), layoutId, parent, false).apply {
            activity = this@SeasonDetailsActivity
            viewModel = this@SeasonDetailsActivity.viewModel
            lifecycleOwner = this@SeasonDetailsActivity
            episode = episodeItem
        }

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()

    }

    private suspend fun collectSeasonDetails() {
        viewModel.seasonDetails.collect { seasonDetails ->
            adapterSeasonCast.submitList(seasonDetails.credits.cast)
            adapterSeasonVideos.submitList(seasonDetails.videos.filterVideos())
            adapterEpisodes.submitList(seasonDetails.episodes)
            adapterSeasonImages.submitList(seasonDetails.images.posters)
        }
    }

    private suspend fun collectEpisodeDetails() {
        viewModel.episodeDetails.collect { episodeDetails ->
            adapterEpisodeVideos.submitList(episodeDetails.videos.filterVideos())
            adapterEpisodeCast.submitList(episodeDetails.credits.cast)
            adapterEpisodeGuestStars.submitList(episodeDetails.credits.guestStars)
            adapterEpisodeImages.submitList(episodeDetails.images.stills)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(state.errorText!!, getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequest(id, seasonNumber)
                }
            }
        }
    }
}