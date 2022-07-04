package com.borabor.movieapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.borabor.movieapp.BuildConfig
import com.borabor.movieapp.data.remote.api.MovieApi
import com.borabor.movieapp.data.remote.api.PersonApi
import com.borabor.movieapp.data.remote.api.TvApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val QUERY_LANGUAGE = "en"
    private const val IMAGE_LANGUAGE = "en,null"

    private const val CACHE_INTERCEPTOR = "cache_interceptor"
    private const val REQUEST_INTERCEPTOR = "request_interceptor"

    private const val CACHE_SIZE = 1024 * 1024 * 10L // 10 MB
    private const val CACHE_MAX_AGE = 60 * 60  // 1 hour
    private const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 // 7 days

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
    }

    @Singleton
    @Provides
    @Named(CACHE_INTERCEPTOR)
    fun provideCacheInterceptor(@ApplicationContext context: Context): Interceptor = Interceptor { chain ->
        val headerName = "Cache-Control"
        val headerValue = if (isNetworkAvailable(context)) "public, max-age=$CACHE_MAX_AGE" else "public, only-if-cached, max-stale=$CACHE_MAX_STALE"

        val request = chain.request()
            .newBuilder()
            .header(headerName, headerValue)
            .build()

        chain.proceed(request)
    }

    @Singleton
    @Provides
    @Named(REQUEST_INTERCEPTOR)
    fun provideRequestInterceptor(): Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()

        val url = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .addQueryParameter("language", QUERY_LANGUAGE)
            .addQueryParameter("include_image_language", IMAGE_LANGUAGE)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()

        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        @Named(CACHE_INTERCEPTOR) cacheInterceptor: Interceptor,
        @Named(REQUEST_INTERCEPTOR) requestInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, CACHE_SIZE))
        .addInterceptor(cacheInterceptor)
        .addInterceptor(requestInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Singleton
    @Provides
    fun provideTvApi(retrofit: Retrofit): TvApi = retrofit.create(TvApi::class.java)

    @Singleton
    @Provides
    fun providePersonApi(retrofit: Retrofit): PersonApi = retrofit.create(PersonApi::class.java)
}