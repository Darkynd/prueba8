package es.uji.al341571.pokeuji.network

import es.uji.al341571.pokeuji.evolutionActivity.EvolutionChain
import es.uji.al341571.pokeuji.pokemonActivity.Pokemon
import es.uji.al341571.pokeuji.speciesActivity.Species
import es.uji.al341571.pokeuji.evolutionActivity.EvolutionChainResponse
import es.uji.al341571.pokeuji.speciesActivity.Flavor

class CachedAPI(private val pokeAPI: PokeAPI) {

    private val pokemonCache: MutableMap<String, Pokemon> = mutableMapOf()
    private val speciesCache: MutableMap<String, Species> = mutableMapOf()
    private val evolutionChainCache: MutableMap<String, EvolutionChain> = mutableMapOf()
    private val evolutionChainUrlCache: MutableMap<String, String> = mutableMapOf()

    suspend fun getPokemon(pokemonName: String): Result<Pokemon> {
        // Checks if the Pokemon is in the cache
        val cachedPokemon = pokemonCache[pokemonName]
        if (cachedPokemon != null) {
            return Result.success(cachedPokemon)
        }

        // If not in cache, get Pokemon from PokeAPI
        return try {
            val pokemonResponse = pokeAPI.getPokemon(pokemonName)
            val pokemon = mapPokemonResponseToPokemon(pokemonResponse)
            pokemonCache[pokemonName] = pokemon
            Result.success(pokemon)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapPokemonResponseToPokemon(pokemonResponse: PokemonResponse): Pokemon {
        // Performs the logic to map a PokemonResponse to a Pokemon object
        return with(pokemonResponse) {
            Pokemon(
                name,
                id,
                name,
                weight,
                height,
                species.name,
                abilities,
                types,
                sprites
            )
        }
    }

    suspend fun getSpecies(id: String): Result<Species> {
        val cachedSpecies = speciesCache[id]
        if (cachedSpecies != null) {
            return Result.success(cachedSpecies)
        }

        return try {
            val speciesResponse = pokeAPI.getSpecies(id)

            val speciesNameEn = speciesResponse.names.find { it.language.name == "en" }?.name.toString()
            val englishFlavorsList = mutableListOf<Flavor>()
            speciesResponse.flavor_text_entries.forEach { flavorTextEntry ->
                if (flavorTextEntry.language.name == "en") {
                    val flavor = Flavor(
                        flavorTextEntry.version.name,
                        flavorTextEntry.flavor_text
                    )
                    englishFlavorsList.add(flavor)
                }
            }

            val species = mapSpeciesResponseToSpecies(speciesResponse, speciesNameEn, englishFlavorsList)
            speciesCache[id] = species
            Result.success(species)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapSpeciesResponseToSpecies(speciesResponse: SpeciesResponse,
                                            speciesNameEn: String,
                                            englishFlavorsList: List<Flavor>): Species {
        return Species(
            speciesResponse.id,
            speciesNameEn,
            speciesResponse.varieties,
            englishFlavorsList
        )
    }

    suspend fun getEvolutionChain(id: String): Result<EvolutionChain> {
        val cachedEvolutionChain = evolutionChainCache[id]
        if (cachedEvolutionChain != null) {
            return Result.success(cachedEvolutionChain)
        }

        return try {
            val evolutionChainResponse = pokeAPI.getEvolutionChain(id)
            val evolutionChain = mapEvolutionChainResponse(evolutionChainResponse)
            evolutionChainCache[id] = evolutionChain
            Result.success(evolutionChain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapEvolutionChainResponse(evolutionChainResponse: EvolutionChainResponse): EvolutionChain {
        return EvolutionChain(evolutionChainResponse)
    }

    suspend fun getEvolutionChainUrl(id: String): Result<String> {
        val cachedUrl = evolutionChainUrlCache[id]
        if (cachedUrl != null) {
            return Result.success(cachedUrl)
        }

        return try {
            val speciesResponse = pokeAPI.getSpecies(id.lowercase())
            val url = speciesResponse.evolution_chain.url
            evolutionChainUrlCache[id] = url
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}