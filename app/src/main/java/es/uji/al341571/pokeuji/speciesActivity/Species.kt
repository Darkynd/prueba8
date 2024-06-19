package es.uji.al341571.pokeuji.speciesActivity

import es.uji.al341571.pokeuji.network.SpeciesVarietyResponse

class Species (
    val id: Int,
    val speciesName: String,
    val varieties: List<SpeciesVarietyResponse>,
    val flavorTexts: List<Flavor>
)