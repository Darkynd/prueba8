package es.uji.al341571.pokeuji.pokemonActivity

interface IPokemon {
    fun showPokemonData(pokemon: Pokemon)
    fun showSearchError(error: Throwable)
    fun showDefaultImage(imageUrl: String?)
    fun showSelectedSprite(imageUrl: String?)
}