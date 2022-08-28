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
    private fun String?.parseYear() = this?.subSequence(0, 4).toString().toInt()
    private fun String?.parseMonth() = this?.subSequence(5, 7).toString().toInt()
    private fun String?.parseDay() = this?.subSequence(8, 10).toString().toInt()

    fun calcCurrentAge(context: Context) = if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        !birthday.isNullOrEmpty() &&
        deathday.isNullOrEmpty()
    ) {
        val currentAge = Period.between(
            LocalDate.of(birthday.parseYear(), birthday.parseMonth(), birthday.parseDay()),
            LocalDate.now()
        ).years

        context.getString(R.string.detail_current_age, currentAge)
    } else ""

    fun calcDeathAge(context: Context) = if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        !birthday.isNullOrEmpty() &&
        !deathday.isNullOrEmpty()
    ) {
        val deathAge = Period.between(
            LocalDate.of(birthday.parseYear(), birthday.parseMonth(), birthday.parseDay()),
            LocalDate.of(deathday.parseYear(), deathday.parseMonth(), deathday.parseDay())
        ).years

        context.getString(R.string.detail_death_age, deathAge)
    } else ""

    fun setGenderText(context: Context) = when (gender) {
        1 -> context.getString(R.string.gender_female)
        2 -> context.getString(R.string.gender_male)
        3 -> context.getString(R.string.gender_non_binary)
        else -> ""
    }

    fun getNames() = alsoKnownAs.joinToString { it }

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