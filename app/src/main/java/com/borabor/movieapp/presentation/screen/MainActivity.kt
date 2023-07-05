package com.borabor.movieapp.presentation.screen

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.borabor.movieapp.R
import com.borabor.movieapp.databinding.ActivityMainBinding
import com.borabor.movieapp.util.Constants
import com.borabor.movieapp.util.isDarkColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (getIsFirstLaunch()) showAlertDialog()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavBar.setupWithNavController(navController)

        updateThemeAndStatusBarOnDestinationChange(binding, navController)
    }

    private fun getIsFirstLaunch(): Boolean = runBlocking {
        dataStore.data.first()[PreferencesKey.IS_FIRST_LAUNCH] ?: true
    }

    private fun setIsFirstLaunch(): Unit = runBlocking {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.IS_FIRST_LAUNCH] = false
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_tmdb_24))
            .setTitle(getString(R.string.tmdb_attribution_title))
            .setMessage(getString(R.string.tmdb_attribution_message))
            .setPositiveButton(getString(R.string.tmdb_attribution_button_text)) { dialog, _ ->
                setIsFirstLaunch()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun updateThemeAndStatusBarOnDestinationChange(
        binding: ActivityMainBinding,
        navController: NavController
    ) {
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            navController.addOnDestinationChangedListener { _, destination, bundle ->
                window.statusBarColor = when (destination.id) {
                    R.id.homeFragment, R.id.searchFragment, R.id.favoritesFragment -> {
                        binding.bottomNavBar.visibility = View.VISIBLE
                        setTheme(R.style.Theme_MovieApp)
                        WindowCompat.setDecorFitsSystemWindows(window, true)
                        isAppearanceLightStatusBars = true

                        when (Build.VERSION.SDK_INT) {
                            21, 22 -> Color.BLACK
                            else -> ContextCompat.getColor(this@MainActivity, R.color.day_night_inverse)
                        }
                    }

                    else -> {
                        binding.bottomNavBar.visibility = View.GONE
                        val backgroundColor = bundle?.getInt(Constants.BACKGROUND_COLOR) ?: 0
                        val isDarkBackground = backgroundColor.isDarkColor()

                        when (destination.id) {
                            R.id.fullscreenImageFragment -> {
                                WindowCompat.setDecorFitsSystemWindows(window, true)
                                Color.BLACK
                            }

                            R.id.seeAllFragment -> {
                                WindowCompat.setDecorFitsSystemWindows(window, true)

                                if (backgroundColor != 0) {
                                    setTheme(if (isDarkBackground) R.style.DetailDarkTheme else R.style.DetailLightTheme)
                                    isAppearanceLightStatusBars = !isDarkBackground
                                    backgroundColor
                                } else {
                                    setTheme(R.style.Theme_MovieApp)
                                    isAppearanceLightStatusBars = true
                                    if (Build.VERSION.SDK_INT in 21..22) Color.BLACK
                                    else ContextCompat.getColor(this@MainActivity, R.color.day_night_inverse)
                                }
                            }

                            else -> {
                                window.statusBarColor = backgroundColor
                                WindowCompat.setDecorFitsSystemWindows(window, false)
                                setTheme(if (isDarkBackground) R.style.DetailDarkTheme else R.style.DetailLightTheme)
                                isAppearanceLightStatusBars = !isDarkBackground
                                Color.TRANSPARENT
                            }
                        }
                    }
                }
            }
        }
    }

    private object PreferencesKey {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }
}