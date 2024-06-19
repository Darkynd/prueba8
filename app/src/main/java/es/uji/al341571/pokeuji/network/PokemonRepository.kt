package es.uji.al341571.pokeuji.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import es.uji.al341571.pokeuji.evolutionActivity.EvolutionChain
import es.uji.al341571.pokeuji.pokemonActivity.Pokemon
import es.uji.al341571.pokeuji.speciesActivity.Flavor
import es.uji.al341571.pokeuji.speciesActivity.Species
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object PokemonRepository {

    // ***** With CacheAPI ***** //
    private val cachedAPI: CachedAPI

    init {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val pokeAPI = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PokeAPI::class.java)

        cachedAPI = CachedAPI(pokeAPI)
    }

    suspend fun getPokemon(pokemonName: String) = cachedAPI.getPokemon(pokemonName.lowercase())
    suspend fun getSpecies(id: String) = cachedAPI.getSpecies(id.lowercase())
    suspend fun getEvolutionChain(id: String) = cachedAPI.getEvolutionChain(id)
    suspend fun getEvolutionChainUrl(id: String) = cachedAPI.getEvolutionChainUrl(id.lowercase())

    // ***** With PokeAPI ***** //
    /*private val api: PokeAPI

    init {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        api = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PokeAPI::class.java)
    }

    suspend fun getPokemon(pokemonName: String) = try {
        withContext(Dispatchers.IO) {
            val pokemonResponse = api.getPokemon(pokemonName.lowercase())

            with(pokemonResponse) {
                Result.success(Pokemon(
                    pokemonName,
                    id,
                    name,
                    weight,
                    height,
                    species.name,
                    abilities,
                    types,
                    sprites))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getSpecies(id: String) = try {
        withContext(Dispatchers.IO) {
            val speciesResponse = api.getSpecies(id.lowercase())
            val speciesNameEn = speciesResponse.names.find { it.language.name == "en" }?.name.toString()
            val englishFlavorsList = mutableListOf<Flavor>()
            speciesResponse.flavor_text_entries.forEach { flavorTextEntry ->
                if (flavorTextEntry.language.name == "en") {
                    val flavor = Flavor(flavorTextEntry.version.name, flavorTextEntry.flavor_text)
                    englishFlavorsList.add(flavor)
                }
            }

            Result.success(Species(
                speciesResponse.id,
                speciesNameEn,
                speciesResponse.varieties,
                englishFlavorsList))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEvolutionChainUrl(id: String): String {
        return withContext(Dispatchers.IO) {
            val speciesResponse = api.getSpecies(id.lowercase())
            val evolutionChainUrlResponse = speciesResponse.evolution_chain.url
            evolutionChainUrlResponse
        }
    }

    suspend fun getEvolutionChain(id: String) = try {
        withContext(Dispatchers.IO) {
            val evolutionChainResponse = api.getEvolutionChain(id)
            Result.success(EvolutionChain(evolutionChainResponse))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }*/
}