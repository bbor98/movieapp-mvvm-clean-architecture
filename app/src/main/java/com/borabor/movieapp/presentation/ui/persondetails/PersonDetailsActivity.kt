package com.borabor.movieapp.presentation.ui.persondetails

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ActivityPersonDetailsBinding
import com.borabor.movieapp.domain.model.MovieCredits
import com.borabor.movieapp.domain.model.TvCredits
import com.borabor.movieapp.presentation.adapter.ImageAdapter
import com.borabor.movieapp.presentation.adapter.MovieAdapter
import com.borabor.movieapp.presentation.adapter.TvAdapter
import com.borabor.movieapp.presentation.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailsActivity : BaseActivity<ActivityPersonDetailsBinding>(R.layout.activity_person_details) {

    private val viewModel: PersonDetailsViewModel by viewModels()

    override val defineBindingVariables: (ActivityPersonDetailsBinding) -> Unit
        get() = { binding ->
            binding.activity = this
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
        }

    val adapterImages = ImageAdapter(isPortrait = true)
    val adapterMovies = MovieAdapter(isCredits = true)
    val adapterTvs = TvAdapter(isCredits = true)

    private lateinit var movieCredits: MovieCredits
    private lateinit var tvCredits: TvCredits

    enum class CreditsType {
        CAST, CREW
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        viewModel.initRequest(id)
        setupMovieCreditsSpinner()
        setupTvCreditsSpinner()
        collectFlows(listOf(::collectDetails, ::collectUiState))
    }

    private fun setupMovieCreditsSpinner() {
        binding.spMovieCredits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var creditsType = CreditsType.CAST
                adapterMovies.submitList(null)

                when (position) {
                    0 -> {
                        adapterMovies.submitList(movieCredits.cast)
                        creditsType = CreditsType.CAST
                    }

                    1 -> {
                        adapterMovies.submitList(movieCredits.crew)
                        creditsType = CreditsType.CREW
                    }
                }

                val title = getString(if (creditsType == CreditsType.CAST) R.string.title_as_cast else R.string.title_as_crew)
                viewModel.setMovieSeeAllList(creditsType, title)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTvCreditsSpinner() {
        binding.spTvCredits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var creditsType = CreditsType.CAST
                adapterTvs.submitList(null)

                when (position) {
                    0 -> {
                        adapterTvs.submitList(tvCredits.cast)
                        creditsType = CreditsType.CAST
                    }

                    1 -> {
                        adapterTvs.submitList(tvCredits.crew)
                        creditsType = CreditsType.CREW
                    }
                }

                val title = getString(if (creditsType == CreditsType.CAST) R.string.title_as_cast else R.string.title_as_crew)
                viewModel.setTvSeeAllList(creditsType, title)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private suspend fun collectDetails() {
        viewModel.details.collect { details ->
            adapterImages.submitList(details.images.profiles)
            movieCredits = details.movieCredits
            tvCredits = details.tvCredits
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(state.errorText!!, getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequest(id)
                }
            }
        }
    }
}