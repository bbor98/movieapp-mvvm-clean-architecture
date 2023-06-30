package com.borabor.movieapp.util

import android.content.Context
import com.borabor.movieapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafeApiCall @Inject constructor(@ApplicationContext val context: Context) {
    suspend inline fun <T> execute(crossinline body: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(
                withContext(Dispatchers.IO) {
                    body()
                }
            )
        } catch (e: Exception) {
            Resource.Error(
                when (e) {
                    is IOException -> context.getString(R.string.error_connection)
                    is HttpException -> {
                        if (e.code() == 504) context.getString(R.string.error_connection)
                        else context.getString(R.string.error_service)
                    }

                    else -> context.getString(R.string.error_unknown)
                }
            )
        }
    }
}