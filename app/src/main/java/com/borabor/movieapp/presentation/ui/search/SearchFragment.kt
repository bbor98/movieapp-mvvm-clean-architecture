package com.borabor.movieapp.presentation.ui.search

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentSearchBinding
import com.borabor.movieapp.presentation.adapter.GenreAdapter
import com.borabor.movieapp.presentation.adapter.MovieAdapter
import com.borabor.movieapp.presentation.adapter.PersonAdapter
import com.borabor.movieapp.presentation.adapter.TvAdapter
import com.borabor.movieapp.presentation.ui.base.BaseFragment
import com.borabor.movieapp.util.LifecycleRecyclerView
import com.borabor.movieapp.util.MediaType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by activityViewModels()

    override val defineBindingVariables: (FragmentSearchBinding) -> Unit
        get() = { binding ->
            binding.fragment = this
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
        }

    val adapterMovies by lazy { MovieAdapter() }
    val adapterTvs by lazy { TvAdapter() }
    val adapterPeople by lazy { PersonAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.apply {
            addObserver(LifecycleRecyclerView(binding.rvGenres))
            addObserver(LifecycleRecyclerView(binding.rvMovies))
            addObserver(LifecycleRecyclerView(binding.rvTvs))
            addObserver(LifecycleRecyclerView(binding.rvPeople))
        }

        setupSearchView()
        setupSpinner()

        collectFlows(
            listOf(
                ::collectMovieSearchResults,
                ::collectTvSearchResults,
                ::collectPersonSearchResults,
                ::collectUiState
            )
        )
    }

    fun clearSearch() {
        viewModel.clearSearchResults()
        adapterMovies.submitList(null)
        adapterTvs.submitList(null)
        adapterPeople.submitList(null)
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvMovies.scrollToPosition(0)
                binding.rvTvs.scrollToPosition(0)
                binding.rvPeople.scrollToPosition(0)

                if (!query.isNullOrEmpty()) viewModel.fetchInitialSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun setupSpinner() {
        binding.spGenreMediaType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        val movieGenreIds = resources.getIntArray(R.array.movie_genre_ids).toTypedArray()
                        val movieGenreNames = resources.getStringArray(R.array.movie_genre_names)
                        binding.rvGenres.adapter = GenreAdapter(MediaType.MOVIE).apply {
                            submitList(movieGenreIds.zip(movieGenreNames))
                        }
                    }

                    1 -> {
                        val tvGenreIds = resources.getIntArray(R.array.tv_genre_ids).toTypedArray()
                        val tvGenreNames = resources.getStringArray(R.array.tv_genre_names)
                        binding.rvGenres.adapter = GenreAdapter(MediaType.TV).apply {
                            submitList(tvGenreIds.zip(tvGenreNames))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private suspend fun collectMovieSearchResults() {
        viewModel.movieResults.collect { movies ->
            adapterMovies.submitList(movies)
        }
    }

    private suspend fun collectTvSearchResults() {
        viewModel.tvResults.collect { tvs ->
            adapterTvs.submitList(tvs)
        }
    }

    private suspend fun collectPersonSearchResults() {
        viewModel.personResults.collect { people ->
            adapterPeople.submitList(people)
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