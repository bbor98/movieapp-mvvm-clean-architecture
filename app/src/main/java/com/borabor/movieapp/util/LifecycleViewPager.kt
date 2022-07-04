package com.borabor.movieapp.util

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2

class LifecycleViewPager(
    private val viewPager2: ViewPager2,
    private val autoSlide: Boolean = false
) : DefaultLifecycleObserver {
    private var handler: Handler? = null
    private val runnable by lazy {
        Runnable {
            viewPager2.apply {
                if (currentItem == 9) currentItem = 0
                else currentItem += 1
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        if (autoSlide) {
            handler = Handler(Looper.getMainLooper())
            viewPager2.registerOnPageChangeCallback(callback)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startAutoSliding()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopAutoSliding()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        viewPager2.adapter = null
    }

    private fun startAutoSliding() {
        handler?.postDelayed(runnable, AUTO_SCROLL_DELAY)
    }

    private fun stopAutoSliding() {
        handler?.removeCallbacks(runnable)
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            stopAutoSliding()
            startAutoSliding()
        }
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 6000L
    }
}