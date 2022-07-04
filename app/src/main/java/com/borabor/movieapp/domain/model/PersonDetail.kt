package com.borabor.movieapp.domain.model

import android.content.Context
import android.os.Build
import com.borabor.movieapp.R
import java.time.LocalDate
import java.time.Period

data class PersonDetail(
    val alsoKnownAs: List<String>,
    val biography: String,
    val birthday: String?,
    val deathday: String?,
    val externalIds: External,
    val gender: Int,
    val homepage: String?,
    val id: Int,
    val images: ImageList,
    val knownForDepartment: String,
    val movieCredits: MovieCredits,
    val name: String,
    val placeOfBirth: String?,
    val profilePath: String?,
    val tvCredits: TvCredits
) {
    fun calcCurrentAge(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !birthday.isNullOrEmpty() && !deathday.isNullOrEmpty()) {
            val birthYear = birthday.subSequence(0, 4).toString().toInt()
            val birthMonth = birthday.subSequence(5, 7).toString().toInt()
            val birthDay = birthday.subSequence(8, 10).toString().toInt()
            val currentAge = Period.between(LocalDate.of(birthYear, birthMonth, birthDay), LocalDate.now()).years

            context.getString(R.string.detail_current_age, currentAge)
        } else ""
    }

    fun calcDeathAge(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !birthday.isNullOrEmpty() && !deathday.isNullOrEmpty()) {
            val birthYear = birthday.subSequence(0, 4).toString().toInt()
            val birthMonth = birthday.subSequence(5, 7).toString().toInt()
            val birthDay = birthday.subSequence(8, 10).toString().toInt()

            val deathYear = deathday.subSequence(0, 4).toString().toInt()
            val deathMonth = deathday.subSequence(5, 7).toString().toInt()
            val deathDay = deathday.subSequence(8, 10).toString().toInt()
            val deathAge = Period.between(LocalDate.of(birthYear, birthMonth, birthDay), LocalDate.of(deathYear, deathMonth, deathDay)).years

            context.getString(R.string.detail_death_age, deathAge)
        } else ""
    }

    fun setGenderText(context: Context): String = when (gender) {
        1 -> context.getString(R.string.gender_female)
        2 -> context.getString(R.string.gender_male)
        3 -> context.getString(R.string.gender_non_binary)
        else -> ""
    }

    fun getNames(): String = alsoKnownAs.joinToString { it }

    companion object {
        val empty = PersonDetail(
            alsoKnownAs = emptyList(),
            biography = "",
            birthday = null,
            deathday = null,
            externalIds = External.empty,
            gender = 0,
            homepage = null,
            id = 0,
            images = ImageList.empty,
            knownForDepartment = "",
            movieCredits = MovieCredits.empty,
            name = "",
            placeOfBirth = null,
            profilePath = null,
            tvCredits = TvCredits.empty
        )
    }
}

data class MovieCredits(
    val cast: List<Movie>,
    val crew: List<Movie>,
) {
    companion object {
        val empty = MovieCredits(
            cast = emptyList(),
            crew = emptyList()
        )
    }
}

data class TvCredits(
    val cast: List<Tv>,
    val crew: List<Tv>,
) {
    companion object {
        val empty = TvCredits(
            cast = emptyList(),
            crew = emptyList()
        )
    }
}