package com.borabor.movieapp.domain.model

data class TvDetail(
    val createdBy: List<Creator>,
    val credits: Credits,
    val episodeRunTime: List<Int>,
    val externalIds: External,
    val firstAirDate: String?,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val images: ImageList,
    val inProduction: Boolean,
    val lastAirDate: String?,
    val name: String,
    val networks: List<Company>,
    val nextEpisodeToAir: Episode?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originalName: String,
    val overview: String?,
    val posterPath: String?,
    val productionCompanies: List<Company>,
    val productionCountries: List<Country>,
    val recommendations: TvList,
    val seasons: List<Season>,
    val spokenLanguages: List<Language>,
    val status: String,
    val videos: VideoList,
    val voteAverage: Double,
    val voteCount: Int
) {
    fun getAiringDates() = if (!firstAirDate.isNullOrEmpty()) {
        if (status != "Ended") firstAirDate.take(4)
        else "${firstAirDate.take(4)} - ${lastAirDate?.take(4)}"
    } else ""

    fun trimCreatorList() = createdBy.joinToString { it.name }
    fun trimGenreList() = genres.joinToString { it.name }
    fun trimNetworkList() = networks.joinToString { it.name + if (it.originCountry.isNotEmpty()) " (${it.originCountry})" else "" }
    fun trimProductionCompanyList() = productionCompanies.joinToString { it.name + if (it.originCountry.isNotEmpty()) " (${it.originCountry})" else "" }
    fun trimProductionCountryList() = productionCountries.joinToString { it.name }
    fun trimSpokenLanguageList() = spokenLanguages.joinToString { it.englishName }

    companion object {
        val empty = TvDetail(
            createdBy = emptyList(),
            credits = Credits.empty,
            episodeRunTime = emptyList(),
            externalIds = External.empty,
            firstAirDate = null,
            genres = emptyList(),
            homepage = null,
            id = 0,
            images = ImageList.empty,
            inProduction = false,
            lastAirDate = null,
            name = "",
            networks = emptyList(),
            nextEpisodeToAir = Episode.empty,
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            originalName = "",
            overview = null,
            posterPath = null,
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            recommendations = TvList.empty,
            seasons = emptyList(),
            spokenLanguages = emptyList(),
            status = "",
            videos = VideoList.empty,
            voteAverage = 0.0,
            voteCount = 0
        )
    }
}

data class Creator(
    val name: String
)

data class Season(
    val airDate: String?,
    val episodeCount: Int,
    val name: String,
    val posterPath: String?,
    val seasonNumber: Int
)