package com.borabor.movieapp.presentation.ui.home

import android.os.Bundle
import android.view.View
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentHomeBinding
import com.borabor.movieapp.presentation.adapter.FragmentAdapter
import com.borabor.movieapp.presentation.ui.base.BaseFragment
import com.borabor.movieapp.util.LifecycleViewPager
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val defineBindingVariables: ((FragmentHomeBinding) -> Unit)?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = FragmentAdapter(this)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleViewPager(binding.viewPager))

        mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabTitles = listOf(getString(R.string.tab_title_1), getString(R.string.tab_title_2))
            tab.text = tabTitles[position]
        }

        mediator?.attach()
    }
}