package com.borabor.movieapp.presentation.ui.movielists

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentMovieListsBinding
import com.borabor.movieapp.presentation.adapter.MovieAdapter
import com.borabor.movieapp.presentation.ui.base.BaseFragment
import com.borabor.movieapp.util.LifecycleRecyclerView
import com.borabor.movieapp.util.LifecycleViewPager
import com.borabor.movieapp.util.playYouTubeVideo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListsFragment : BaseFragment<FragmentMovieListsBinding>(R.layout.fragment_movie_lists) {

    private val viewModel: MovieListsViewModel by activityViewModels()

    override val defineBindingVariables: (FragmentMovieListsBinding) -> Unit
        get() = { binding ->
            binding.fragment = this
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
        }

    val adapterTrending = MovieAdapter(isTrending = true) { playTrailer(it) }
    val adapterPopular = MovieAdapter()
    val adapterTopRated = MovieAdapter()
    val adapterNowPlaying = MovieAdapter()
    val adapterUpcoming = MovieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.apply {
            addObserver(LifecycleViewPager(binding.vpTrendings, true))
            addObserver(LifecycleRecyclerView(binding.rvPopular))
            addObserver(LifecycleRecyclerView(binding.rvTopRated))
            addObserver(LifecycleRecyclerView(binding.rvNowPlaying))
            addObserver(LifecycleRecyclerView(binding.rvUpcoming))
        }

        setupSpinner()

        collectFlows(
            listOf(
                ::collectTrendingMovies,
                ::collectPopularMovies,
                ::collectTopRatedMovies,
                ::collectNowPlayingMovies,
                ::collectUpcomingMovies,
                ::collectUiState
            )
        )
    }

    private fun playTrailer(movieId: Int) {
        val videoKey = viewModel.getTrendingMovieTrailerKey(movieId)
        if (videoKey.isEmpty()) showSnackbar(
            message = getString(R.string.trending_trailer_error),
            indefinite = false,
            anchor = true
        ) else activity?.playYouTubeVideo(videoKey)
    }

    private fun setupSpinner() {
        val items = resources.getStringArray(R.array.viable_countries)
        val spinnerAdapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items) {
            override fun isEnabled(position: Int): Boolean = position != 0

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                view.text = items[position].split(",")[0]
                return view
            }
        }

        val listener = object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
            var isTouch = false

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) return

                val selectedItem = parent?.getItemAtPosition(position).toString().split(",")
                val countryName = selectedItem[0]
                val countryCode = selectedItem[1]

                view?.let { (it as TextView).text = countryName }

                if (isTouch) viewModel.getNowPlayingMoviesInSelectedRegion(countryName, countryCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                isTouch = true
                return false
            }
        }

        binding.spNowPlaying.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = listener
            setOnTouchListener(listener)
        }
    }

    private suspend fun collectTrendingMovies() {
        viewModel.trendingMovies.collect { trendingMovies ->
            adapterTrending.submitList(trendingMovies.take(10))
        }
    }

    private suspend fun collectPopularMovies() {
        viewModel.popularMovies.collect { popularMovies ->
            adapterPopular.submitList(popularMovies)
        }
    }

    private suspend fun collectTopRatedMovies() {
        viewModel.topRatedMovies.collect { topRatedMovies ->
            adapterTopRated.submitList(topRatedMovies)
        }
    }

    private suspend fun collectNowPlayingMovies() {
        viewModel.nowPlayingMovies.collect { nowPlayingMovies ->
            adapterNowPlaying.submitList(nowPlayingMovies)
        }
    }

    private suspend fun collectUpcomingMovies() {
        viewModel.upcomingMovies.collect { upcomingMovies ->
            adapterUpcoming.submitList(upcomingMovies)
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