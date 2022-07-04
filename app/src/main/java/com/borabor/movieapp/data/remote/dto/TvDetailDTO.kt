package com.borabor.movieapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TvDetailDTO(
    @SerializedName("created_by")
    val createdBy: List<CreatorDTO>,
    @SerializedName("credits")
    val credits: CreditsDTO,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>,
    @SerializedName("external_ids")
    val externalIds: ExternalDTO,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("genres")
    val genres: List<GenreDTO>,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: ImageListDTO,
    @SerializedName("in_production")
    val inProduction: Boolean,
    @SerializedName("last_air_date")
    val lastAirDate: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("networks")
    val networks: List<CompanyDTO>,
    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: EpisodeDTO?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<CompanyDTO>,
    @SerializedName("production_countries")
    val productionCountries: List<CountryDTO>,
    @SerializedName("recommendations")
    val recommendations: TvListDTO,
    @SerializedName("seasons")
    val seasons: List<SeasonDTO>,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<LanguageDTO>,
    @SerializedName("status")
    val status: String,
    @SerializedName("videos")
    val videos: VideoListDTO,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)

data class CreatorDTO(
    @SerializedName("name")
    val name: String
)

data class SeasonDTO(
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int
)