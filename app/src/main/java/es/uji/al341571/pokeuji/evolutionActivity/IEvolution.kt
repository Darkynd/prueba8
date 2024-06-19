package es.uji.al341571.pokeuji.evolutionActivity

interface IEvolution {
    fun showEvolutionChainData(evolutionChain: EvolutionChain)
    fun showSearchError(error: Throwable)
}