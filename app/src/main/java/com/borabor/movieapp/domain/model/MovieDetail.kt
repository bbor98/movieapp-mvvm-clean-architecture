package com.borabor.movieapp.domain.model

data class MovieDetail(
    val budget: Long,
    val credits: Credits,
    val externalIds: External,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val images: ImageList,
    val originalTitle: String,
    val overview: String?,
    val posterPath: String?,
    val productionCompanies: List<Company>,
    val productionCountries: List<Country>,
    val recommendations: MovieList,
    val releaseDate: String?,
    val revenue: Long,
    val runtime: Int?,
    val spokenLanguages: List<Language>,
    val status: String,
    val title: String,
    val videos: VideoList,
    val voteAverage: Double,
    val voteCount: Int
) {
    fun trimGenreList() = genres.joinToString { it.name }
    fun trimProductionCompanyList() = productionCompanies.joinToString { it.name + if (it.originCountry.isNotEmpty()) " (${it.originCountry})" else "" }
    fun trimProductionCountryList() = productionCountries.joinToString { it.name }
    fun trimSpokenLanguageList() = spokenLanguages.joinToString { it.englishName }

    companion object {
        val empty = MovieDetail(
            budget = 0L,
            credits = Credits.empty,
            externalIds = External.empty,
            genres = emptyList(),
            homepage = null,
            id = 0,
            images = ImageList.empty,
            originalTitle = "",
            overview = null,
            posterPath = null,
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            recommendations = MovieList.empty,
            releaseDate = null,
            revenue = 0L,
            runtime = null,
            spokenLanguages = emptyList(),
            status = "",
            title = "",
            videos = VideoList.empty,
            voteAverage = 0.0,
            voteCount = 0
        )
    }
}