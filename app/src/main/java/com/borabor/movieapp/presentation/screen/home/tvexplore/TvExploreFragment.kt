package com.borabor.movieapp.presentation.screen.home.tvexplore

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentTvExploreBinding
import com.borabor.movieapp.presentation.adapter.TvAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.playYouTubeVideo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvExploreFragment : BaseFragment<FragmentTvExploreBinding>(R.layout.fragment_tv_explore) {

    override val viewModel: TvExploreViewModel by viewModels()

    val adapterTrending = TvAdapter(isTrending = true) { playTrailer(it) }
    val adapterPopular = TvAdapter()
    val adapterTopRated = TvAdapter()
    val adapterAiring = TvAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageViewPagerAdapterLifecycle(binding.vpTrendings, true)

        manageRecyclerViewAdapterLifecycle(
            binding.rvPopular,
            binding.rvTopRated,
            binding.rvAiring
        )

        setupSpinner()

        collectFlows(
            listOf(
                ::collectTrendingTvs,
                ::collectPopularTvs,
                ::collectTopRatedTvs,
                ::collectAiringTvs,
                ::collectUiState
            )
        )
    }

    private fun playTrailer(tvId: Int) {
        val videoKey = viewModel.getTrendingTvTrailerKey(tvId)
        if (videoKey.isEmpty()) showSnackbar(
            message = getString(R.string.trending_trailer_error),
            indefinite = false,
            anchor = true
        ) else playYouTubeVideo(videoKey)
    }

    private fun setupSpinner() {
        val listener = object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
            var isTouch = false

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTouch) {
                    val airingTime = if (position == 0) Constants.LIST_ID_AIRING_DAY else Constants.LIST_ID_AIRING_WEEK
                    viewModel.switchAiringTime(airingTime)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                isTouch = true
                return false
            }
        }

        binding.spAiring.setOnTouchListener(listener)
        binding.spAiring.onItemSelectedListener = listener
    }

    private suspend fun collectTrendingTvs() {
        viewModel.trendingTvShows.collect { trendingTvs ->
            adapterTrending.submitList(trendingTvs.take(10))
        }
    }

    private suspend fun collectPopularTvs() {
        viewModel.popularTvShows.collect { popularTvs ->
            adapterPopular.submitList(popularTvs)
        }
    }

    private suspend fun collectTopRatedTvs() {
        viewModel.topRatedTvShows.collect { topRatedTvs ->
            adapterTopRated.submitList(topRatedTvs)
        }
    }

    private suspend fun collectAiringTvs() {
        viewModel.airingTvShows.collect { airingTvs ->
            adapterAiring.submitList(airingTvs)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(
                message = state.errorText!!,
                actionText = getString(R.string.button_retry),
                anchor = true
            ) {
                viewModel.retryConnection {
                    viewModel.initRequests()
                }
            }
        }
    }
}