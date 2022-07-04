package com.borabor.movieapp.domain.repository

import com.borabor.movieapp.domain.model.PersonDetail
import com.borabor.movieapp.domain.model.PersonList
import com.borabor.movieapp.util.Resource

interface PersonRepository {
    suspend fun getPersonSearchResults(query: String, page: Int): Resource<PersonList>
    suspend fun getPersonDetails(personId: Int): Resource<PersonDetail>
}