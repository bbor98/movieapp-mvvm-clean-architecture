package com.borabor.movieapp.presentation.screen.fullscreenimage

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.FragmentFullscreenImageBinding
import com.borabor.movieapp.presentation.adapter.FullscreenImageAdapter
import com.borabor.movieapp.presentation.base.BaseFragment
import com.borabor.movieapp.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FullscreenImageFragment : BaseFragment<FragmentFullscreenImageBinding>(R.layout.fragment_fullscreen_image) {

    val isFullscreen = MutableStateFlow(false)
    val imagePositionText = MutableStateFlow("")

    override val viewModel: BaseViewModel?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FullscreenImageFragmentArgs by navArgs()
        val imageList = args.imageList.toList()
        val imagePosition = args.imagePosition
        val totalImageCount = imageList.size

        binding.vpImages.apply {
            adapter = FullscreenImageAdapter { toggleUiVisibility() }.apply { submitList(imageList) }
            setCurrentItem(imagePosition, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    imagePositionText.value = "${position + 1}/$totalImageCount"
                }
            })
        }
    }

    private fun toggleUiVisibility() {
        if (isFullscreen.value) showUi() else hideUi()
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        isFullscreen.value = true
    }

    private fun showUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).show(WindowInsetsCompat.Type.systemBars())

        isFullscreen.value = false
    }
}