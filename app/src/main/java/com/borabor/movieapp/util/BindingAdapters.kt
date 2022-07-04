package com.borabor.movieapp.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.borabor.movieapp.R
import com.borabor.movieapp.domain.model.Genre
import com.borabor.movieapp.presentation.ui.moviedetails.MovieDetailsActivity
import com.borabor.movieapp.presentation.ui.persondetails.PersonDetailsActivity
import com.borabor.movieapp.presentation.ui.seasondetails.SeasonDetailsActivity
import com.borabor.movieapp.presentation.ui.seeall.SeeAllActivity
import com.borabor.movieapp.presentation.ui.tvdetails.TvDetailsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import jp.wasabeef.glide.transformations.CropTransformation
import net.cachapa.expandablelayout.ExpandableLayout

@BindingAdapter("isVisible")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:layout_width")
fun View.setWidth(width: Float) {
    layoutParams.width = width.toInt()
}

@BindingAdapter("android:layout_height")
fun View.setHeight(height: Float) {
    layoutParams.height = height.toInt()
}

@BindingAdapter("detailMediaType", "detailId", "detailImageUrl", "seasonNumber", requireAll = false)
fun View.setDetailsIntent(mediaType: MediaType, id: Int, imageUrl: String?, seasonNumber: Int?) {
    var backgroundColor = ContextCompat.getColor(context, R.color.day_night_inverse)

    imageUrl?.let {
        Glide.with(context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w92$it")
            .priority(Priority.HIGH)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate().dominantSwatch?.rgb?.let { backgroundColor = it }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    setOnClickListener {
        val intentClass = when (mediaType) {
            MediaType.MOVIE -> MovieDetailsActivity::class.java
            MediaType.TV -> if (seasonNumber == null) TvDetailsActivity::class.java else SeasonDetailsActivity::class.java
            MediaType.PERSON -> PersonDetailsActivity::class.java
        }

        Intent(context, intentClass).apply {
            putExtra(Constants.DETAIL_ID, id)
            putExtra(Constants.BACKGROUND_COLOR, backgroundColor)
            if (seasonNumber != null) putExtra(Constants.SEASON_NUMBER, seasonNumber)

            context.startActivity(this)
        }
    }
}

@BindingAdapter("intentType", "mediaType", "intId", "stringId", "title", "backgroundColor", "region", "list", "isLandscape", requireAll = false)
fun View.setSeeAllIntent(
    intentType: IntentType,
    mediaType: MediaType?,
    detailId: Int?,
    listId: String?,
    title: String,
    backgroundColor: Int,
    region: String?,
    list: List<Any>?,
    isLandscape: Boolean?
) {
    setOnClickListener {
        Intent(context, SeeAllActivity::class.java).apply {
            putExtra(Constants.INTENT_TYPE, intentType as Parcelable)
            putExtra(Constants.TITLE, title)
            putExtra(Constants.BACKGROUND_COLOR, backgroundColor)
            if (mediaType != null) putExtra(Constants.MEDIA_TYPE, mediaType as Parcelable)
            if (detailId != null) putExtra(Constants.DETAIL_ID, detailId)
            if (listId != null) putExtra(Constants.LIST_ID, listId)
            if (region != null) putExtra(Constants.REGION, region)
            if (list != null) putExtra(Constants.LIST, ArrayList(list))
            if (isLandscape != null) putExtra(Constants.IS_LANDSCAPE, isLandscape)

            context.startActivity(this)
        }
    }
}

@BindingAdapter("android:layout_marginBottom", "isImage", requireAll = false)
fun View.setLayoutMarginBottom(isGrid: Boolean, isImage: Boolean) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.bottomMargin = resources.getDimension(
        if (isGrid) {
            if (isImage) R.dimen.bottom_margin_small else R.dimen.bottom_margin
        } else R.dimen.zero_dp
    ).toInt()

    layoutParams = params
}

@BindingAdapter("android:background")
fun View.setBackground(color: Int) {
    setBackgroundColor(if (color != 0) color else ContextCompat.getColor(context, R.color.day_night_inverse))
}

@BindingAdapter("transparentBackground")
fun View.setTransparentBackground(backgroundColor: Int) {
    setBackgroundColor(ColorUtils.setAlphaComponent(backgroundColor, 220))
}

@BindingAdapter("isNested")
fun ViewPager2.handleNestedScroll(isNested: Boolean) {
    if (isNested) {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        recyclerView.interceptTouch()
    }
}

@BindingAdapter("isNested")
fun RecyclerView.handleNestedScroll(isNested: Boolean) {
    if (isNested) interceptTouch()
}

@BindingAdapter("type", "isGrid", "loadMore", "shouldLoadMore", requireAll = false)
fun RecyclerView.addInfiniteScrollListener(type: Any?, isGrid: Boolean, infiniteScroll: InfiniteScrollListener, shouldLoadMore: Boolean) {
    if (shouldLoadMore) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val layoutManagerType = if (isGrid) layoutManager as GridLayoutManager else layoutManager as LinearLayoutManager
            private val visibleThreshold = 10
            private var loading = true
            private var previousTotal = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val visibleItemCount = layoutManagerType.childCount
                val totalItemCount = layoutManagerType.itemCount
                val firstVisibleItem = layoutManagerType.findFirstVisibleItemPosition()

                if (totalItemCount < previousTotal) previousTotal = 0

                if (loading && totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    infiniteScroll.onLoadMore(type)
                    loading = true
                }
            }
        })
    }
}

@BindingAdapter("fixedSize")
fun RecyclerView.setFixedSize(hasFixedSize: Boolean) {
    setHasFixedSize(hasFixedSize)
}

@SuppressLint("CheckResult")
@BindingAdapter("imageUrl", "imageMediaType", "imageQuality", "centerCrop", "fitTop", "isThumbnail", requireAll = false)
fun ImageView.loadImage(posterPath: String?, mediaType: MediaType?, quality: ImageQuality?, centerCrop: Boolean?, fitTop: Boolean, isThumbnail: Boolean) {
    val imageUrl = if (isThumbnail) "https://img.youtube.com/vi/$posterPath/0.jpg" else quality?.imageBaseUrl + posterPath

    val errorImage = AppCompatResources.getDrawable(
        context,
        when (mediaType) {
            MediaType.MOVIE -> R.drawable.ic_baseline_movie_24
            MediaType.TV -> R.drawable.ic_baseline_live_tv_24
            MediaType.PERSON -> R.drawable.ic_baseline_person_24
            null -> R.drawable.ic_baseline_image_24
        }
    )

    val glide = Glide.with(context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(errorImage)
        .skipMemoryCache(false)

    if (centerCrop == true) glide.centerCrop()
    if (fitTop) glide.apply(RequestOptions.bitmapTransform(CropTransformation(0, 1235, CropTransformation.CropType.TOP)))

    glide.into(this)
}

@BindingAdapter("iconTint")
fun ImageView.setIconTint(color: Int?) {
    color?.let { setColorFilter(it.setTintColor()) }
}

@BindingAdapter("externalPlatform", "externalId")
fun ImageView.setExternals(externalPlatform: ExternalPlatform, externalId: String?) {
    val uri = (externalPlatform.url ?: "") + externalId
    val packageName = externalPlatform.packageName

    setOnClickListener {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)).setPackage(packageName))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }
}

@BindingAdapter("coloredRating")
fun TextView.setRatingColor(rating: Double) {
    text = rating.toString()
    setTextColor(
        ContextCompat.getColor(
            context,
            when {
                rating >= 9.0 -> R.color.nine_to_ten
                rating >= 8.0 -> R.color.eight_to_nine
                rating >= 7.0 -> R.color.seven_to_eight
                rating >= 6.0 -> R.color.six_to_seven
                rating >= 5.0 -> R.color.five_to_six
                rating >= 4.0 -> R.color.four_to_five
                rating >= 3.0 -> R.color.three_to_four
                rating >= 2.0 -> R.color.two_to_three
                rating >= 1.0 -> R.color.one_to_two
                rating > 0.0 -> R.color.zero_to_one
                else -> R.color.zero
            }
        )
    )
}

@BindingAdapter("activity", "backArrowTint", "seeAllTitle", "titleTextColor", requireAll = false)
fun Toolbar.setupToolbar(activity: AppCompatActivity, backArrowTint: Int, seeAllTitle: String?, titleTextColor: Int?) {
    activity.apply {
        setSupportActionBar(this@setupToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (seeAllTitle != null) supportActionBar?.title = seeAllTitle
    }

    navigationIcon?.setTint(if (backArrowTint != 0) backArrowTint.setTintColor() else ContextCompat.getColor(context, R.color.day_night))
    if (titleTextColor != null) setTitleTextColor(if (titleTextColor != 0) titleTextColor.setTintColor() else ContextCompat.getColor(context, R.color.day_night))

    setNavigationOnClickListener {
        activity.finish()
    }
}

@BindingAdapter("collapsingToolbar", "frameLayout", "toolbarTitle", "backgroundColor", requireAll = false)
fun AppBarLayout.setToolbarCollapseListener(collapsingToolbar: CollapsingToolbarLayout, frameLayout: FrameLayout, toolbarTitle: String, backgroundColor: Int) {
    var isShow = true
    var scrollRange = -1
    this.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (scrollRange == -1) {
            scrollRange = appBarLayout?.totalScrollRange!!
        }

        if (scrollRange + verticalOffset == 0) {
            frameLayout.isVisible = false
            collapsingToolbar.setCollapsedTitleTextColor(backgroundColor.setTintColor())
            collapsingToolbar.title = toolbarTitle
            isShow = true
        } else if (isShow) {
            frameLayout.isVisible = isShow
            collapsingToolbar.title = " "
            isShow = false
        }
    })
}

@BindingAdapter("expand", "expandIcon")
fun ConstraintLayout.setExpandableLayout(expandableLayout: ExpandableLayout, expandIcon: ImageView) {
    setOnClickListener {
        expandableLayout.toggle()
        expandIcon.animate().rotationBy(-180f)
        isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ isClickable = true }, 600)
    }
}

@BindingAdapter("genreMediaType", "genres", "chipTint", requireAll = false)
fun ChipGroup.setGenreChips(mediaType: MediaType, genreList: List<Genre>?, backgroundColor: Int) {
    genreList?.let {
        it.forEach { genre ->
            addView(
                Chip(context).apply {
                    setChipBackgroundColorResource(if (backgroundColor.isDarkColor()) R.color.white else R.color.black)
                    text = genre.name
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setTextColor(backgroundColor.setTintColor(true))
                    setOnClickListener {
                        Intent(context, SeeAllActivity::class.java).apply {
                            putExtra(Constants.INTENT_TYPE, IntentType.GENRE as Parcelable)
                            putExtra(Constants.MEDIA_TYPE, mediaType as Parcelable)
                            putExtra(Constants.DETAIL_ID, genre.id)
                            putExtra(Constants.TITLE, genre.name)

                            context.startActivity(this)
                        }
                    }
                })
        }
    }
}