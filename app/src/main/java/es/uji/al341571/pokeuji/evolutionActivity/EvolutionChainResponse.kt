package es.uji.al341571.pokeuji.evolutionActivity

class EvolutionChainResponse (
    val chain: ChainResponse
)

data class ChainResponse(
    val species: SpeciesResponse,
    val evolves_to: List<EvolvesToResponse>?
)

data class EvolvesToResponse(
    val species: SpeciesResponse?,
    val evolves_to: List<EvolvesToResponse>?
)

data class SpeciesResponse(
    val name: String
)

