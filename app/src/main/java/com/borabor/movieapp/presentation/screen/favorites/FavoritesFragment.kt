package com.borabor.movieapp.presentation.screen.favorites

import android.os.Bundle
import android.view.View
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentFavoritesBinding
import com.borabor.movieapp.presentation.adapter.FragmentAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.presentation.base.BaseViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(R.layout.fragment_favorites) {

    override val viewModel: BaseViewModel?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = FragmentAdapter(this)
        manageViewPagerAdapterLifecycle(binding.viewPager, false)

        mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabTitles = listOf(getString(R.string.tab_title_1), getString(R.string.tab_title_2))
            tab.text = tabTitles[position]
        }

        mediator?.attach()
    }
}