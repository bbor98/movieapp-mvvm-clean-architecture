package com.borabor.movieapp.presentation.bindingadapter

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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.borabor.movieapp.R
import com.borabor.movieapp.domain.model.Image
import com.borabor.movieapp.domain.model.Movie
import com.borabor.movieapp.domain.model.Person
import com.borabor.movieapp.domain.model.Tv
import com.borabor.movieapp.domain.model.Video
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.Content
import com.borabor.movieapp.util.Detail
import com.borabor.movieapp.util.ExternalPlatform
import com.borabor.movieapp.util.ImageQuality
import com.borabor.movieapp.util.InfiniteScrollListener
import com.borabor.movieapp.util.interceptTouch
import com.borabor.movieapp.util.setTintColor
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
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

@BindingAdapter("detailType", "detailId", "detailImageUrl", "seasonNumber", "episodeNumber", requireAll = false)
fun View.setDetailsNavigation(
    detailType: Detail,
    id: Int,
    imageUrl: String?,
    seasonNumber: Int?,
    episodeNumber: Int?
) {
    var backgroundColor = ContextCompat.getColor(context, R.color.day_night_inverse)

    imageUrl?.let {
        Glide.with(context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w92$it")
            .priority(Priority.HIGH)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate().dominantSwatch?.rgb?.let { color ->
                        backgroundColor = color
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    setOnClickListener {
        val destination = when (detailType) {
            Detail.MOVIE -> R.id.action_global_movieDetailsFragment
            Detail.TV -> R.id.action_global_tvDetailsFragment
            Detail.TV_SEASON -> R.id.action_tvDetailsFragment_to_seasonDetailsFragment
            Detail.TV_EPISODE -> R.id.action_seasonDetailsFragment_to_episodeDetailsFragment
            Detail.PERSON -> R.id.action_global_personDetailsFragment
        }

        val bundle = bundleOf(
            Constants.DETAIL_ID to id,
            Constants.BACKGROUND_COLOR to backgroundColor,
            Constants.SEASON_NUMBER to seasonNumber,
            Constants.EPISODE_NUMBER to episodeNumber
        )

        findNavController().navigate(destination, bundle)
    }
}

@BindingAdapter(
    "contentType", "seeAllDetailType", "genreId", "stringId", "title", "backgroundColor", "region", "videoList", "castList",
    "imageList", "personMovieCreditsList", "personTvCreditsList", "movieRecommendationsList", "tvRecommendationsList",
    requireAll = false
)
fun View.setSeeAllNavigation(
    contentType: Content,
    detailType: Detail?,
    genreId: Int?,
    stringId: String?,
    title: String,
    backgroundColor: Int,
    region: String?,
    videoList: List<Video>?,
    castList: List<Person>?,
    imageList: List<Image>?,
    personMovieCreditsList: List<Movie>?,
    personTvCreditsList: List<Tv>?,
    movieRecommendationsList: List<Movie>?,
    tvRecommendationsList: List<Tv>?
) {
    setOnClickListener {
        val bundle = bundleOf(
            Constants.CONTENT_TYPE to contentType as Parcelable,
            Constants.DETAIL_TYPE to detailType as Parcelable?,
            Constants.GENRE_ID to (genreId ?: 0),
            Constants.LIST_ID to stringId,
            Constants.TITLE to title,
            Constants.BACKGROUND_COLOR to backgroundColor,
            Constants.REGION to region,
            Constants.VIDEO_LIST to videoList?.toTypedArray(),
            Constants.IMAGE_LIST to imageList?.toTypedArray(),
            Constants.CAST_LIST to castList?.toTypedArray(),
            Constants.PERSON_MOVIE_CREDITS_LIST to personMovieCreditsList?.toTypedArray(),
            Constants.PERSON_TV_CREDITS_LIST to personTvCreditsList?.toTypedArray(),
            Constants.MOVIE_RECOMMENDATIONS_LIST to movieRecommendationsList?.toTypedArray(),
            Constants.TV_RECOMMENDATIONS_LIST to tvRecommendationsList?.toTypedArray()
        )

        findNavController().navigate(R.id.action_global_seeAllFragment, bundle)
    }
}

@BindingAdapter("android:layout_marginBottom", "isImage", requireAll = false)
fun View.setLayoutMarginBottom(
    isGrid: Boolean,
    isImage: Boolean
) {
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
fun RecyclerView.addInfiniteScrollListener(
    type: Any?,
    isGrid: Boolean,
    infiniteScroll: InfiniteScrollListener,
    shouldLoadMore: Boolean
) {
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
@BindingAdapter("imageUrl", "imageQuality", "centerCrop", "fitTop", "isThumbnail", "errorImage", requireAll = false)
fun ImageView.loadImage(
    posterPath: String?,
    quality: ImageQuality?,
    centerCrop: Boolean?,
    fitTop: Boolean,
    isThumbnail: Boolean,
    errorImage: Drawable?
) {
    val imageUrl = if (isThumbnail) "https://img.youtube.com/vi/$posterPath/0.jpg" else quality?.imageBaseUrl + posterPath

    val glide = Glide.with(context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(errorImage ?: AppCompatResources.getDrawable(context, R.drawable.ic_baseline_image_24))
        .dontAnimate()

    if (centerCrop == true) glide.centerCrop()
    if (fitTop) glide.apply(RequestOptions.bitmapTransform(CropTransformation(0, 1235, CropTransformation.CropType.TOP)))

    glide.into(this)
}

@BindingAdapter("iconTint")
fun ImageView.setIconTint(color: Int?) {
    color?.let { setColorFilter(it.setTintColor()) }
}

@BindingAdapter("externalPlatform", "externalId")
fun ImageView.setExternals(
    externalPlatform: ExternalPlatform,
    externalId: String?
) {
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

@BindingAdapter("fragment", "backArrowTint", "toolbarTitle", "toolbarTitleColor", requireAll = false)
fun Toolbar.setupToolbar(
    fragment: Fragment,
    backArrowTint: Int,
    title: String?,
    titleColor: Int?
) {
    with(fragment.requireActivity() as AppCompatActivity) {
        setSupportActionBar(this@setupToolbar)
        if (title != null) setTitle(title)
    }

    setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

    navigationIcon?.setTint(
        if (backArrowTint != 0) backArrowTint.setTintColor()
        else ContextCompat.getColor(context, R.color.day_night)
    )

    if (titleColor != null) {
        setTitleTextColor(
            if (titleColor != 0) titleColor.setTintColor()
            else ContextCompat.getColor(context, R.color.day_night)
        )
    }

    setNavigationOnClickListener {
        fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}

@BindingAdapter("collapsingToolbar", "frameLayout", "collapsingToolbarTitle", "backgroundColor", requireAll = false)
fun AppBarLayout.setToolbarCollapseListener(
    collapsingToolbar: CollapsingToolbarLayout,
    frameLayout: FrameLayout,
    collapsingToolbarTitle: String,
    backgroundColor: Int
) {
    var isShow = true
    var scrollRange = -1

    addOnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (scrollRange == -1) scrollRange = appBarLayout?.totalScrollRange!!

        if (scrollRange + verticalOffset == 0) {
            frameLayout.isVisible = false
            collapsingToolbar.setCollapsedTitleTextColor(backgroundColor.setTintColor())
            collapsingToolbar.title = collapsingToolbarTitle
            isShow = true
        } else if (isShow) {
            frameLayout.isVisible = isShow
            collapsingToolbar.title = " "
            isShow = false
        }
    }
}

@BindingAdapter("expand", "expandIcon")
fun ConstraintLayout.setExpandableLayout(
    expandableLayout: ExpandableLayout,
    expandIcon: ImageView
) {
    setOnClickListener {
        expandableLayout.toggle()
        expandIcon.animate().rotationBy(-180f)
        isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ isClickable = true }, 600)
    }
}