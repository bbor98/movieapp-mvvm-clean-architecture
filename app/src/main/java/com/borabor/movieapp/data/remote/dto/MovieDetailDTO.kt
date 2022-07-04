package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MovieDetailDTO(
    @SerializedName("budget")
    val budget: Long,
    @SerializedName("credits")
    val credits: CreditsDTO,
    @SerializedName("external_ids")
    val externalIds: ExternalDTO,
    @SerializedName("genres")
    val genres: List<GenreDTO>,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<CompanyDTO>,
    @SerializedName("production_countries")
    val productionCountries: List<CountryDTO>,
    @SerializedName("recommendations")
    val recommendations: MovieListDTO,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("revenue")
    val revenue: Long,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<LanguageDTO>,
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("videos")
    val videos: VideoListDTO,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)