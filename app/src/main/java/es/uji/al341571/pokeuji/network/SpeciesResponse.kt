package es.uji.al341571.pokeuji.network

class SpeciesResponse(
    val id: Int,
    val names: List<SpeciesNameResponse>,
    val varieties: List<SpeciesVarietyResponse>,
    val flavor_text_entries: List<FlavorTextResponse>,

    val evolution_chain: EvolutionChainResponse
)

data class SpeciesNameResponse(val language: LanguageResponse, val name: String)
data class LanguageResponse(val name: String)

data class SpeciesVarietyResponse(val pokemon: PokemonVarietyResponse)
data class PokemonVarietyResponse(val name: String)

data class FlavorTextResponse(val flavor_text: String, val language: FlavorLanguageResponse, val version: VersionResponse)
data class FlavorLanguageResponse(val name: String)
data class VersionResponse(val name: String)

data class EvolutionChainResponse (val url: String)

