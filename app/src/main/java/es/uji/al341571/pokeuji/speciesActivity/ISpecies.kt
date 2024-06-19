package es.uji.al341571.pokeuji.speciesActivity

interface ISpecies {
    fun showSpeciesData(species: Species?)
    fun showSearchError(error: Throwable)
    fun showVersions(versionNames: List<Flavor>): List<String>
}