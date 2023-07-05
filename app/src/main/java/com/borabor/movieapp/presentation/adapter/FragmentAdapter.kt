package com.borabor.movieapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.borabor.movieapp.presentation.screen.favorites.FavoritesFragment
import com.borabor.movieapp.presentation.screen.favorites.favoritemovies.FavoriteMoviesFragment
import com.borabor.movieapp.presentation.screen.favorites.favoritetvs.FavoriteTvsFragment
import com.borabor.movieapp.presentation.screen.home.HomeFragment
import com.borabor.movieapp.presentation.screen.home.movieexplore.MovieExploreFragment
import com.borabor.movieapp.presentation.screen.home.tvexplore.TvExploreFragment
import com.borabor.movieapp.util.Constants

class FragmentAdapter(
    private val fragment: Fragment
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {
    private val homeFragments = listOf(MovieExploreFragment(), TvExploreFragment())
    private val favoritesFragments = listOf(FavoriteMoviesFragment(), FavoriteTvsFragment())

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (fragment) {
            is HomeFragment -> homeFragments[position]
            is FavoritesFragment -> favoritesFragments[position]
            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_FRAGMENT_TYPE)
        }
    }
}