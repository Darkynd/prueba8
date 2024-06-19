package es.uji.al341571.pokeuji.speciesActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.uji.al341571.pokeuji.databinding.ActivitySpeciesBinding
import es.uji.al341571.pokeuji.evolutionActivity.EvolutionActivity
import es.uji.al341571.pokeuji.evolutionActivity.EvolutionActivity.Companion.EVOLUTION_NAME
import es.uji.al341571.pokeuji.pokemonActivity.PokemonActivity
import org.chromium.net.NetworkException

class SpeciesActivity : AppCompatActivity(), ISpecies {

    private val viewModel: SpeciesViewModel by viewModels()
    private lateinit var binding: ActivitySpeciesBinding

    private lateinit var versionDescriptions: List<String>
    private var currentVariety: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflates the layout using view binding
        binding = ActivitySpeciesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Associates the ViewModel instance (viewModel) with the current view (this),
        // which in this context is the SpeciesActivity.
        viewModel.view = this

        // Get the species name from the intent and display current Species
        val speciesName = intent.getStringExtra(SPECIES_NAME)
        currentVariety = intent.getStringExtra(CURRENT_VARIETY).toString()

        // Call loadSpecies
        viewModel.loadSpecies(speciesName.toString())

        binding.evolutionButton.setOnClickListener {
            val intent = Intent(this, EvolutionActivity::class.java)
            intent.putExtra(EVOLUTION_NAME, speciesName)
            startActivity(intent)
        }
    }

    override fun showSpeciesData(species: Species?) {
        if (species != null) {
            binding.speciesNameDisplay.text = species.speciesName
        }

        // Varieties: RecyclerView Settings
        // Convert List<SpeciesVarietyResponse> to into Variety objects means class Variety
        val varietyList = mutableListOf<Variety>()
        // Manages the position of the icon that identifies the current variety
        var indexDefault = 0
        species?.varieties?.forEachIndexed { index, speciesVariety ->
            if(speciesVariety.pokemon.name == currentVariety) {
                indexDefault = index
            }
        }
        species?.varieties?.forEachIndexed { index, speciesVariety ->
            val variety = if (index == indexDefault) {
                Variety(speciesVariety.pokemon.name, true)
            } else {
                Variety(speciesVariety.pokemon.name, false)
            }
            varietyList.add(variety)
        }

        // RecyclerView Adapter
        binding.varietiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = species?.let { _ ->
                VarietyAdapter(varietyList) { variety ->
                    onVarietyClicked(variety)
                }
            }
        }

        // Spinner Settings
        versionDescriptions = species?.let { showVersions(it.flavorTexts) } ?: emptyList()
        binding.speciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val description = versionDescriptions[position]
                binding.descriptionParagraphDisplay.text = description
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun showVersions(versionNames: List<Flavor>): List<String> {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            versionNames.map { it.version }) // Map to get only the version names
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.speciesSpinner.adapter = adapter
        return versionNames.map { it.description }
    }

    override fun showSearchError(error: Throwable) {
        Log.e("Species Activity", "Error during Species search", error)
        if (error is NetworkException) {
            showToast("Network error: ${error.message}")
        } else {
            val errorMessage = error.message?: "Unknown error"
            showToast(errorMessage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Change to PokemonActivity
    private fun onVarietyClicked(varietyClicked: Variety) {

        currentVariety = varietyClicked.name

        val intent = Intent(this, PokemonActivity::class.java)
        intent.putExtra(PokemonActivity.POKEMON_ID_FROM_SPECIES, binding.speciesNameDisplay.text)
        intent.putExtra(PokemonActivity.POKEMON_SPECIES_VARIETY, varietyClicked.name)
        intent.putExtra(PokemonActivity.IS_INTENT_FROM_SPECIES, true)
        startActivity(intent)
    }

    companion object {
        const val SPECIES_NAME = "SPECIES_NAME"
        const val CURRENT_VARIETY = "current_variety"
    }
}