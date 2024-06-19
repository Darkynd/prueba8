package es.uji.al341571.pokeuji.network

import es.uji.al341571.pokeuji.evolutionActivity.EvolutionChainResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PokeAPI {

    @Headers("Accept: application/json")
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") name: String): PokemonResponse

    @Headers("Accept: application/json")
    @GET("pokemon-species/{id}")
    suspend fun getSpecies(@Path("id") name: String): SpeciesResponse

    @Headers("Accept: application/json")
    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") name: String): EvolutionChainResponse
}