package es.uji.al341571.pokeuji.pokemonActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.pokeuji.network.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel: ViewModel() {

    private val basePathToDefaultSprite: String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    var view: IPokemon? = null
        set(value) {
            field = value
            if(value != null)
                pokemon?.let { displayPokemon(it) }
        }

    private var pokemon: Pokemon? = null
        set(value) {
            field = value
            if(value != null)
                displayPokemon(value)
        }

    private fun displayPokemon(pokemon: Pokemon) {
        view?.showPokemonData(pokemon)
        view?.showDefaultImage(getPokemonImageUrl(pokemon.pokemonId))
    }

    fun onPokemonSearchRequested(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            PokemonRepository.getPokemon(id)
                .onSuccess { pokemon = it}
                .onFailure { view?.showSearchError(it) }
        }
    }

    private fun getPokemonImageUrl(pokemonId: Int): String {
        // Gets the URL of the Pokemon's image based on its ID
        return "$basePathToDefaultSprite$pokemonId.png"
    }

    fun getPokemonAbilities(): List<String> {
        val abilitiesList = mutableListOf<String>()
        pokemon?.abilities?.forEach { ability ->
            abilitiesList.add(ability.ability.name.capitalizeFirstLetter())
        }
        return abilitiesList
    }

    fun getPokemonTypes(): List<String> {
        val typesList = mutableListOf<String>()
        pokemon?.types?.forEach { type ->
            typesList.add(type.type.name.capitalizeFirstLetter())
        }
        return typesList
    }

    fun getPokemonSprites(): List<String> {
        val spriteNameList = mutableListOf<String>()
        pokemon?.spritesMap?.forEach { sprite ->
            spriteNameList.add(sprite.name)
        }
        return spriteNameList
    }

    fun onSpriteSelected(spriteSelected: String) {
        val spriteToLoad = pokemon?.spritesMap?.find { it.name == spriteSelected }
        spriteToLoad?.let { sprite ->
            view?.showSelectedSprite(sprite.url)
        }
    }

    private fun String.capitalizeFirstLetter(): String {
        return if (isNotEmpty()) {
            this[0].uppercaseChar() + substring(1)
        } else {
            this
        }
    }
}
