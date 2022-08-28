package com.borabor.movieapp.domain.model

data class Credits(
    val cast: List<Person>,
    val crew: List<Person>,
    val guestStars: List<Person>?
) {
    fun getDirector() = crew.find { it.job == "Director" }?.name ?: ""
    fun getWriters() = crew.filter { it.department == "Writing" }.joinToString { it.name + " (${it.job})" }

    companion object {
        val empty = Credits(
            cast = emptyList(),
            crew = emptyList(),
            guestStars = null
        )
    }
}