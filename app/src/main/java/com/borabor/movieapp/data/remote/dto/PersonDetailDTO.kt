package com.borabor.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PersonDetailDTO(
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>,
    @SerializedName("biography")
    val biography: String,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("deathday")
    val deathday: String?,
    @SerializedName("external_ids")
    val externalIds: ExternalDTO,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("movie_credits")
    val movieCredits: MovieCreditsDTO,
    @SerializedName("name")
    val name: String,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("tv_credits")
    val tvCredits: TvCreditsDTO
)

data class MovieCreditsDTO(
    @SerializedName("cast")
    val cast: List<MovieDTO>,
    @SerializedName("crew")
    val crew: List<MovieDTO>
)

data class TvCreditsDTO(
    @SerializedName("cast")
    val cast: List<TvDTO>,
    @SerializedName("crew")
    val crew: List<TvDTO>
)