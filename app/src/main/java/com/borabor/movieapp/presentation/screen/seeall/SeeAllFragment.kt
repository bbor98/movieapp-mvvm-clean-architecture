package com.borabor.movieapp.presentation.screen.seeall

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentSeeAllBinding
import com.borabor.movieapp.domain.model.Image
import com.borabor.movieapp.domain.model.Movie
import com.borabor.movieapp.domain.model.Person
import com.borabor.movieapp.domain.model.Tv
import com.borabor.movieapp.domain.model.Video
import com.borabor.movieapp.presentation.adapter.ImageAdapter
import com.borabor.movieapp.presentation.adapter.MovieAdapter
import com.borabor.movieapp.presentation.adapter.PersonAdapter
import com.borabor.movieapp.presentation.adapter.TvAdapter
import com.borabor.movieapp.presentation.adapter.VideoAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.Content
import com.borabor.movieapp.util.Detail
import com.borabor.movieapp.util.playYouTubeVideo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeAllFragment : BaseFragment<FragmentSeeAllBinding>(R.layout.fragment_see_all) {

    override val viewModel: SeeAllViewModel by viewModels()

    private val movieAdapter by lazy { MovieAdapter(isGrid = true) }
    private val tvAdapter by lazy { TvAdapter(isGrid = true) }
    private val personAdapter by lazy { PersonAdapter(isGrid = true) }

    private var detailType: Parcelable? = null
    private var genreId: Int? = null
    private var listId: String? = null
    private var region: String? = null
    private var videoList: List<Video>? = null
    private var castList: List<Person>? = null
    private var imageList: List<Image>? = null
    private var personMovieCreditsList: List<Movie>? = null
    private var personTvCreditsList: List<Tv>? = null
    private var movieRecommendationsList: List<Movie>? = null
    private var tvRecommendationsList: List<Tv>? = null

    var contentType: Parcelable? = null
    var title: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        getList()

        binding.rvSeeAll.layoutManager = GridLayoutManager(
            requireContext(),
            if (
                contentType == Content.VIDEOS ||
                contentType == Content.IMAGES && detailType != Detail.TV_SEASON && detailType != Detail.PERSON
            ) 2
            else 3
        )

        manageRecyclerViewAdapterLifecycle(binding.rvSeeAll)
    }

    private fun getArgs() {
        val args: SeeAllFragmentArgs by navArgs()

        contentType = args.contentType
        detailType = args.detailType
        genreId = args.genreId
        listId = args.listId
        region = args.region
        videoList = args.videoList?.toList()
        castList = args.castList?.toList()
        imageList = args.imageList?.toList()
        personMovieCreditsList = args.personMovieCreditsList?.toList()
        personTvCreditsList = args.personTvCreditsList?.toList()
        movieRecommendationsList = args.movieRecommendationsList?.toList()
        tvRecommendationsList = args.tvRecommendationsList?.toList()
        title = args.title +
                if (contentType == Content.GENRE) {
                    " " + if (detailType == Detail.MOVIE) getString(R.string.title_movies) else getString(R.string.title_tv_shows)
                } else ""
    }

    private fun getList() {
        binding.rvSeeAll.adapter = when (contentType) {
            Content.VIDEOS -> {
                VideoAdapter(true) { videoKey ->
                    playYouTubeVideo(videoKey)
                }.apply {
                    submitList(videoList)
                }
            }

            Content.CAST -> {
                PersonAdapter(isGrid = true, isCast = true).apply {
                    submitList(castList)
                }
            }

            Content.IMAGES -> {
                ImageAdapter(
                    isPortrait = detailType == Detail.PERSON || detailType == Detail.TV_SEASON,
                    isGrid = true
                ).apply {
                    submitList(imageList)
                }
            }

            Content.PERSON_CREDITS -> when (detailType) {
                Detail.MOVIE -> {
                    MovieAdapter(isGrid = true, isCredits = true).apply {
                        submitList(personMovieCreditsList)
                    }
                }

                Detail.TV -> {
                    TvAdapter(isGrid = true, isCredits = true).apply {
                        submitList(personTvCreditsList)
                    }
                }

                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_DETAIL_TYPE)
            }

            Content.RECOMMENDATIONS -> {
                when (detailType) {
                    Detail.MOVIE -> {
                        MovieAdapter(isGrid = true).apply {
                            submitList(movieRecommendationsList)
                        }
                    }

                    Detail.TV -> {
                        TvAdapter(isGrid = true).apply {
                            submitList(tvRecommendationsList)
                        }
                    }

                    else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_DETAIL_TYPE)
                }
            }

            else -> {
                when (detailType) {
                    Detail.MOVIE -> movieAdapter
                    Detail.TV -> tvAdapter
                    else -> personAdapter
                }.also {
                    collectFlows(
                        listOf(
                            ::collectListResult,
                            ::collectUiState
                        )
                    )
                }
            }
        }

        viewModel.initRequest(contentType, detailType, genreId, listId, region)
    }

    private suspend fun collectListResult() {
        viewModel.results.collect { results ->
            when (detailType) {
                Detail.MOVIE -> {
                    val movieList = results as Set<Movie>
                    movieAdapter.submitList(movieList.toList())
                }

                Detail.TV -> {
                    val tvList = results as Set<Tv>
                    tvAdapter.submitList(tvList.toList())
                }

                Detail.PERSON -> {
                    val personList = results as Set<Person>
                    personAdapter.submitList(personList.toList())
                }
            }
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(message = state.errorText!!, actionText = getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequest(contentType, detailType, id, listId, region)
                }
            }
        }
    }
}