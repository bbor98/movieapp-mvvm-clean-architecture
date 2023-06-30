package com.borabor.movieapp.di

import com.borabor.movieapp.data.repository.MovieRepositoryImpl
import com.borabor.movieapp.data.repository.PersonRepositoryImpl
import com.borabor.movieapp.data.repository.TvRepositoryImpl
import com.borabor.movieapp.domain.repository.MovieRepository
import com.borabor.movieapp.domain.repository.PersonRepository
import com.borabor.movieapp.domain.repository.TvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun bindTvRepository(repository: TvRepositoryImpl): TvRepository

    @Binds
    abstract fun bindPersonRepository(repository: PersonRepositoryImpl): PersonRepository
}