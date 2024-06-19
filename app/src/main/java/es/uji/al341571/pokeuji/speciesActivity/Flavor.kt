package es.uji.al341571.pokeuji.speciesActivity

class Flavor(val version: String, description: String) {
    val description = convertDescription(description)

    private fun convertDescription(description: String) = description
        .replace('\n', ' ')
        .replace("\u000c", " ")
        .replace('é', 'E')
}