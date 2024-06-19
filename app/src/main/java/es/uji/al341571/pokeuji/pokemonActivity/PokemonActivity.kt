package es.uji.al341571.pokeuji.pokemonActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import es.uji.al341571.pokeuji.R
import es.uji.al341571.pokeuji.databinding.ActivityMainBinding
import es.uji.al341571.pokeuji.speciesActivity.SpeciesActivity
import es.uji.al341571.pokeuji.speciesActivity.SpeciesActivity.Companion.CURRENT_VARIETY
import es.uji.al341571.pokeuji.speciesActivity.SpeciesActivity.Companion.SPECIES_NAME
import org.chromium.net.NetworkException
import java.util.Locale

class PokemonActivity : AppCompatActivity(), IPokemon {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PokemonViewModel by viewModels()

    // From SpeciesActivity
    private var isIntentFromSpecies: Boolean = false
    private var pokemonSpeciesVariety: String = ""
    private lateinit var pokemonIdFromSpecies: String

    // From EvolutionActivity
    private var isIntentFromEvolution: Boolean = false
    private lateinit var pokemonIdFromEvolution: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchEditText.addTextChangedListener { _ ->
            binding.searchEditText.setTextColor(Color.BLACK)
        }

        binding.searchButton.setOnClickListener {
            val pokemonNameEntered = binding.searchEditText.text.toString()
            viewModel.onPokemonSearchRequested(pokemonNameEntered)
        }

        binding.pokemonImage.setOnClickListener {
            if(binding.pokemonSpeciesDisplay.text != getString(R.string.unknownSpecies)) {
                val dialogSpritesFragment = PokemonActivitySpritesDialog()
                dialogSpritesFragment.show(supportFragmentManager, "sprites_dialog")
            } else {
                showToast("Unknown Pokemon")
            }
        }

        // Change to SpeciesActivity
        binding.pokemonSpeciesDisplay.setOnClickListener {
            if(binding.pokemonSpeciesDisplay.text != getString(R.string.unknownSpecies)) {
                val currentSpeciesName = binding.pokemonNameDisplay.text.toString()
                val intent = Intent(this, SpeciesActivity::class.java)
                intent.putExtra(SPECIES_NAME, currentSpeciesName)
                intent.putExtra(CURRENT_VARIETY, pokemonSpeciesVariety)
                startActivity(intent)
            } else {
                showToast("Unknown Species")
            }
        }

        binding.abilitiesButton.setOnClickListener {
            if(binding.pokemonNameDisplay.text != getString(R.string.unknownPokemonName)) {
                val dialogAbilitiesFragment = PokemonActivityDialog.newInstance(binding.pokemonNameDisplay.text.toString(), true)
                dialogAbilitiesFragment.setViewModel(viewModel)
                dialogAbilitiesFragment.show(supportFragmentManager, "abilities_dialog")
            } else {
                showToast("Unknown Pokemon")
            }
        }

        binding.typesButton.setOnClickListener {
            if(binding.pokemonNameDisplay.text != getString(R.string.unknownPokemonName)) {
            val dialogTypesFragment = PokemonActivityDialog.newInstance(binding.pokemonNameDisplay.text.toString(), false)
            dialogTypesFragment.setViewModel(viewModel)
            dialogTypesFragment.show(supportFragmentManager, "types_dialog")
            } else {
                showToast("Unknown Pokemon")
            }
        }

        // Intents
        // Intent: From Species Activity
        pokemonIdFromSpecies = intent.getStringExtra(POKEMON_ID_FROM_SPECIES).toString()
        pokemonSpeciesVariety = intent.getStringExtra(POKEMON_SPECIES_VARIETY).toString()
        isIntentFromSpecies = intent.getBooleanExtra(IS_INTENT_FROM_SPECIES, false)

        if(isIntentFromSpecies)
            viewModel.onPokemonSearchRequested(pokemonIdFromSpecies)

        // Intent: From Evolution Activity
        pokemonIdFromEvolution = intent.getStringExtra(POKEMON_ID_FROM_EVOLUTION).toString()
        isIntentFromEvolution = intent.getBooleanExtra(IS_INTENT_FROM_EVOLUTION, false)

        if(isIntentFromEvolution) {
            isIntentFromEvolution = false
            viewModel.onPokemonSearchRequested(pokemonIdFromEvolution)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    override fun showPokemonData(pokemon: Pokemon) {
        pokemon.let {
            with (binding) {
                // Displays the Pokemon's first letter name in uppercase.
                pokemonNameDisplay.text = it.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }

                val pokemonWeight = "${(it.weight / 10f)} kg"
                val pokemonHeight = "${(it.height / 10f)} m"
                pokemonWeightDisplay.text = pokemonWeight
                pokemonHeightDisplay.text = pokemonHeight

                if(isIntentFromSpecies) {
                    binding.pokemonSpeciesDisplay.text = pokemonSpeciesVariety
                    isIntentFromSpecies = false
                } else {
                    pokemonSpeciesDisplay.text = it.speciesName
                }
            }
        }
    }

    override fun showSearchError(error: Throwable) {
        // Log the error
        Log.e("PokemonActivity", "Error during Pokemon search", error)
        // Handle the error based on its type
        if (error is NetworkException) {
            // Specific network error
            showToast("Network error: ${error.message}")
        } else {
            val errorMessage = error.message?: "Unknown error"
            showToast(errorMessage)
        }
        resetPokemon()
    }

    override fun showDefaultImage(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty())
            binding.pokemonImage.setImageDrawable(null)
        else
            Glide.with(this@PokemonActivity)
                .load(imageUrl)
                .fitCenter()
                .into(binding.pokemonImage)
    }

    override fun showSelectedSprite(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty())
            binding.pokemonImage.setImageDrawable(null)
        else
            Glide.with(this@PokemonActivity)
                .load(imageUrl)
                .fitCenter()
                .into(binding.pokemonImage)
    }

    @SuppressLint("Recycle")
    private fun resetPokemon() {
        with (binding) {
            searchEditText.setTextColor(Color.RED)
            pokemonNameDisplay.text = getString(R.string.unknownPokemonName)
            pokemonWeightDisplay.text = getString(R.string.unknownPokemonWeight)
            pokemonHeightDisplay.text = getString(R.string.unknownPokemonHeight)
            pokemonSpeciesDisplay.text = getString(R.string.unknownSpecies)

            // Get the image resource identifier of the custom style attribute
            val alertDialogIconId = obtainStyledAttributes(intArrayOf(R.attr.customAlertDialogIcon))
            val resourceId = alertDialogIconId.getResourceId(0, 0)
            // Set the ImageView image with the obtained image resource identifier
            if (resourceId != 0) {
                // Set the ImageView image with the obtained image resource identifier
                pokemonImage.setImageResource(resourceId)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val IS_INTENT_FROM_SPECIES = "is_intent_from_species"
        const val IS_INTENT_FROM_EVOLUTION = "is_intent_from_evolution"
        const val POKEMON_ID_FROM_SPECIES = "pokemon_id_from_species"
        const val POKEMON_ID_FROM_EVOLUTION = "pokemon_id_from_evolution"
        const val POKEMON_SPECIES_VARIETY = "pokemon_species_variety"
    }
}