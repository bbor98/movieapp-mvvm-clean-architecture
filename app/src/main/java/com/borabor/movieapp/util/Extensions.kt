package com.borabor.movieapp.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.borabor.movieapp.BuildConfig
import com.borabor.movieapp.R
import com.google.android.youtube.player.YouTubeStandalonePlayer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

fun Activity.playYouTubeVideo(videoKey: String) {
    startActivity(
        YouTubeStandalonePlayer.createVideoIntent(
            this,
            BuildConfig.YOUTUBE_API_KEY,
            videoKey,
            0, // start millisecond
            true, // autoplay
            false // lightbox mode
        )
    )
}

fun RecyclerView.interceptTouch() {
    addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return canScrollHorizontally(RecyclerView.FOCUS_FORWARD).let {
                rv.parent.requestDisallowInterceptTouchEvent(it && e.action == MotionEvent.ACTION_MOVE)
                if (!it) removeOnItemTouchListener(this)
                !it
            }
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    })
}

fun Int.isDarkColor(): Boolean {
    val darkness = 1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return darkness >= 0.5
}

fun Int.setTintColor(reverse: Boolean = false): Int = if (this.isDarkColor() xor reverse) Color.WHITE else Color.BLACK

fun Int?.formatTime(context: Context): String? = this?.let {
    when {
        it == 0 -> return null
        it >= 60 -> {
            val hours = it / 60
            val minutes = it % 60
            "${hours}${context.getString(R.string.hour_short)} ${if (minutes == 0) "" else "$minutes${context.getString(R.string.minute_short)}"}"
        }

        else -> "${it}${context.getString(R.string.minute_short)}"
    }
}

fun Long.thousandsSeparator(context: Context): String = with(DecimalFormatSymbols()) {
    groupingSeparator = context.getString(R.string.thousand_separator).toCharArray()[0]
    DecimalFormat("#,###", this).format(this@thousandsSeparator)
}

fun Double.round(): Double = (this * 10.0).roundToInt() / 10.0

fun Double.asPercent(): String = "%${(this * 10).toInt()}"

fun String?.formatDate(): String {
    val outputFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.US)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return if (!this.isNullOrEmpty()) outputFormat.format(inputFormat.parse(this)) else ""
}