package es.uji.al341571.pokeuji.evolutionActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.pokeuji.network.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvolutionViewModel: ViewModel() {

    var view: IEvolution? = null
        set(value) {
            field = value
            if(value != null)
                evolutionChain?.let { displayEvolution(it) }
        }

    private var evolutionChain: EvolutionChain? = null
        set(value) {
            field = value
            if(value != null)
                displayEvolution(value)
        }

    private fun displayEvolution(evolutionChain: EvolutionChain) {
        view?.showEvolutionChainData(evolutionChain)
    }

    fun loadEvolutionChain(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val evolutionChainUrlResult = PokemonRepository.getEvolutionChainUrl(id)

                if (evolutionChainUrlResult.isSuccess) {
                    val evolutionChainUrl = evolutionChainUrlResult.getOrNull()
                    if (evolutionChainUrl != null) {
                        val evolutionChainId = evolutionChainUrl.split("/").takeLast(2).joinToString("/")
                        PokemonRepository.getEvolutionChain(evolutionChainId)
                            .onSuccess { evolutionChain = it }
                            .onFailure { view?.showSearchError(it) }
                    } else {
                        view?.showSearchError(Exception("Evolution Chain URL is null"))
                    }
                } else {
                    val exception = evolutionChainUrlResult.exceptionOrNull()
                    println("Error: Failed to get Evolution Chain URL: $exception")
                    view?.showSearchError(exception ?: Exception("Failed to get Evolution Chain URL"))
                }

            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
            }
        }
    }
}