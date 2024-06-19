package es.uji.al341571.pokeuji.speciesActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.pokeuji.network.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpeciesViewModel: ViewModel() {

    var view: ISpecies? = null
        set(value) {
            field = value
            if(value != null)
                species?.let { displaySpecies(it) }
        }

    var species: Species? = null
        set(value) {
            field = value
            if(value != null)
                displaySpecies(value)
        }

    private fun displaySpecies(species: Species) {
        view?.showSpeciesData(species)
        view?.showVersions(species.flavorTexts)
    }

    fun loadSpecies(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            PokemonRepository.getSpecies(id)
                .onSuccess { species = it }
                .onFailure { view?.showSearchError(it) }
        }
    }
}
