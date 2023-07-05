package com.borabor.movieapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.borabor.movieapp.R
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.LifecycleRecyclerView
import com.borabor.movieapp.util.LifecycleViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0


abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutId: Int) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    abstract val viewModel: BaseViewModel?

    protected var mediator: TabLayoutMediator? = null

    private var snackbar: Snackbar? = null

    protected val detailId by lazy { arguments?.getInt(Constants.DETAIL_ID)!! }

    val backgroundColor by lazy { arguments?.getInt(Constants.BACKGROUND_COLOR)!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate<B>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.fragment, this@BaseFragment)
            setVariable(BR.viewModel, viewModel)
        }

        return binding.root
    }

    protected fun manageRecyclerViewAdapterLifecycle(vararg recyclerViews: RecyclerView) {
        recyclerViews.forEach { recyclerView ->
            viewLifecycleOwner.lifecycle.addObserver(LifecycleRecyclerView(recyclerView))
        }
    }

    protected fun manageViewPagerAdapterLifecycle(viewPager: ViewPager2, autoSlide: Boolean) {
        viewLifecycleOwner.lifecycle.addObserver(LifecycleViewPager(viewPager, autoSlide))
    }

    protected fun collectFlows(collectors: List<KSuspendFunction0<Unit>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectors.forEach { collector ->
                    launch {
                        collector()
                    }
                }
            }
        }
    }

    protected fun showSnackbar(
        message: String,
        indefinite: Boolean = true,
        actionText: String? = null,
        anchor: Boolean = false,
        action: (() -> Unit)? = null
    ) {
        val view = activity?.window?.decorView?.rootView
        val length = if (indefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        val snackbar = view?.let { Snackbar.make(it, message, length) }

        if (action != null) snackbar?.setAction(actionText) { action() }
        if (anchor) snackbar?.setAnchorView(R.id.bottom_nav_bar)

        this.snackbar = snackbar
        this.snackbar?.show()
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        with(requireActivity() as AppCompatActivity) {
            if (actionBar != null) setSupportActionBar(null)
        }

        mediator?.detach()
        mediator = null

        _binding = null
    }
}