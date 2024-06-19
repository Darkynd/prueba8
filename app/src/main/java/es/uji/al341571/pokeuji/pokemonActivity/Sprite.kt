package es.uji.al341571.pokeuji.pokemonActivity

import es.uji.al341571.pokeuji.network.SpritesResponse

class Sprite (val name: String, val url: String)

fun getSprites(sprites: SpritesResponse): List<Sprite> {
    val spriteList = mutableListOf<Sprite>()

    with(sprites) {
        if (back_default != null) spriteList.add(Sprite("Back Default", back_default))
        if (back_female != null) spriteList.add(Sprite("Back Female", back_female))
        if (back_shiny != null) spriteList.add(Sprite("Back Shiny", back_shiny))
        if (back_shiny_female != null) spriteList.add(Sprite("Back Shiny Female", back_shiny_female))
        if (front_default != null) spriteList.add(Sprite("Front Default", front_default))
        if (front_female != null) spriteList.add(Sprite("Front Female", front_female))
        if (front_shiny != null) spriteList.add(Sprite("Front Shiny", front_shiny))
        if (front_shiny_female != null) spriteList.add(Sprite("Front Shiny Female", front_shiny_female))
    }

    with(sprites.other) {
        this?.home?.let {
            if (it.front_default != null) spriteList.add(Sprite("Home: Front Default", it.front_default))
            if (it.front_female != null) spriteList.add(Sprite("Home: Front Female", it.front_female))
            if (it.front_shiny != null) spriteList.add(Sprite("Home: Front Shiny", it.front_shiny))
            if (it.front_shiny_female != null) spriteList.add(Sprite("Home: Front Shiny Female", it.front_shiny_female))
        }
        this?.official_artwork?.let {
            if (it.front_default != null) spriteList.add(Sprite("Official-Artwork: Front Default", it.front_default))
            if (it.front_shiny != null) spriteList.add(Sprite("Official-Artwork: Front Shiny", it.front_shiny))
        }
        this?.showdown?.let {
            if (it.back_default != null) spriteList.add(Sprite("Showdown: Back Default", it.back_default))
            if (it.back_female != null) spriteList.add(Sprite("Showdown: Back Female", it.back_female))
            if (it.back_shiny != null) spriteList.add(Sprite("Showdown: Back Shiny", it.back_shiny))
            if (it.back_shiny_female != null) spriteList.add(Sprite("Showdown: Back Shiny Female", it.back_shiny_female))
            if (it.front_default != null) spriteList.add(Sprite("Showdown: Front Default", it.front_default))
            if (it.front_female != null) spriteList.add(Sprite("Showdown: Front Female", it.front_female))
            if (it.front_shiny != null) spriteList.add(Sprite("Showdown: Front Shiny", it.front_shiny))
            if (it.front_shiny_female != null) spriteList.add(Sprite("Showdown: Front Shiny Female", it.front_shiny_female))
        }
    }

    return spriteList
}