package es.uji.al341571.pokeuji.pokemonActivity

import es.uji.al341571.pokeuji.network.AbilityResponse
import es.uji.al341571.pokeuji.network.SpritesResponse
import es.uji.al341571.pokeuji.network.TypesResponse

class Pokemon (
    val id: String,
    val pokemonId: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val speciesName: String,
    val abilities: List<AbilityResponse>,
    val types: List<TypesResponse>,
    private val sprites: SpritesResponse,

    val spritesMap: List<Sprite> = getSprites(sprites)
)


