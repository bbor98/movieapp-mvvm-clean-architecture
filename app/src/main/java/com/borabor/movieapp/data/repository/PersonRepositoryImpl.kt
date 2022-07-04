package com.borabor.movieapp.data.repository

import com.borabor.movieapp.data.mapper.toPersonDetail
import com.borabor.movieapp.data.mapper.toPersonList
import com.borabor.movieapp.data.remote.api.PersonApi
import com.borabor.movieapp.domain.model.PersonDetail
import com.borabor.movieapp.domain.model.PersonList
import com.borabor.movieapp.domain.repository.PersonRepository
import com.borabor.movieapp.util.Resource
import com.borabor.movieapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val api: PersonApi,
    private val safeApiCall: SafeApiCall
) : PersonRepository {
    override suspend fun getPersonSearchResults(query: String, page: Int): Resource<PersonList> = safeApiCall.execute {
        api.getPersonSearchResults(query, page).toPersonList()
    }

    override suspend fun getPersonDetails(personId: Int): Resource<PersonDetail> = safeApiCall.execute {
        api.getPersonDetails(personId).toPersonDetail()
    }
}