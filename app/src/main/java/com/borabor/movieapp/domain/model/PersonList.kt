package com.borabor.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PersonList(
    val results: List<Person>,
    val totalResults: Int
) {
    companion object {
        val empty = PersonList(
            results = emptyList(),
            totalResults = 0
        )
    }
}

@Parcelize
data class Person(
    val character: String?,
    val department: String?,
    val id: Int,
    val job: String?,
    val knownForDepartment: String?,
    val name: String,
    val profilePath: String?
) : Parcelable