package es.uji.al341571.pokeuji.network

import com.squareup.moshi.Json

class PokemonResponse (
    val name: String,
    val id: Int,
    val weight: Int,
    val height: Int,
    val species: PokemonSpeciesResponse,
    val abilities: List<AbilityResponse>,
    val types: List<TypesResponse>,
    val sprites: SpritesResponse
)

data class PokemonSpeciesResponse (val name: String)

data class AbilityResponse (val ability: AbilityInfoResponse)
data class AbilityInfoResponse (val name: String)

data class TypesResponse (val type: TypeInfoResponse)
data class TypeInfoResponse (val name: String)

data class SpritesResponse (
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?,
    val other: OtherSpritesResponse?,
)

data class OtherSpritesResponse (
    val home: HomeSpritesResponse?,
    @Json(name = "official-artwork")
    val official_artwork: OfficialArtworkSpritesResponse?,
    val showdown: ShowdownSpritesResponse?
)

data class HomeSpritesResponse (
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class OfficialArtworkSpritesResponse (
    val front_default: String?,
    val front_shiny: String?
)

data class ShowdownSpritesResponse (
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)



