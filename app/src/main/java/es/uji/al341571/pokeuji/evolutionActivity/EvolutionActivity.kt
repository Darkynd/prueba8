package es.uji.al341571.pokeuji.evolutionActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import es.uji.al341571.pokeuji.databinding.ActivityEvolutionBinding
import es.uji.al341571.pokeuji.pokemonActivity.PokemonActivity
import org.chromium.net.NetworkException

class EvolutionActivity: AppCompatActivity(), IEvolution {

    private val viewModel: EvolutionViewModel by viewModels()
    private lateinit var binding: ActivityEvolutionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEvolutionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.view = this

        val evolutionName = intent.getStringExtra(EVOLUTION_NAME)
        evolutionName?.let { viewModel.loadEvolutionChain(it) }
    }

    override fun showEvolutionChainData(evolutionChain: EvolutionChain) {

        val evolutionChainTree = generateEvolutionChainTree(evolutionChain)

        binding.evolutionChainDisplay.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = EvolutionChainTreeAdapter(evolutionChainTree) { node ->
                onEvolutionNodeClicked(node)
            }
        }
    }

    private fun onEvolutionNodeClicked(node: EvolutionChainTree) {

        val intent = Intent(this, PokemonActivity::class.java)
        intent.putExtra(PokemonActivity.POKEMON_ID_FROM_EVOLUTION, node.speciesName)
        intent.putExtra(PokemonActivity.IS_INTENT_FROM_EVOLUTION, true)

        startActivity(intent)
    }

    override fun showSearchError(error: Throwable) {
        Log.e("Evolution Activity", "Error during Evolution search", error)
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

    companion object {
        const val EVOLUTION_NAME = "EVOLUTION_NAME"
    }
}