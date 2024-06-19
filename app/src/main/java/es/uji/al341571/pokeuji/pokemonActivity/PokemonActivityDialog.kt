package es.uji.al341571.pokeuji.pokemonActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PokemonActivityDialog: DialogFragment() {

    private lateinit var viewModel: PokemonViewModel
    private var showAbilities: Boolean = false

    fun setViewModel(viewModel: PokemonViewModel) {
        this.viewModel = viewModel
    }

    fun setShowAbilities(showAbilities: Boolean) {
        this.showAbilities = showAbilities
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pokemonName = arguments?.getString("pokemonName")?: ""
        val items = if(showAbilities) {
            viewModel.getPokemonAbilities().toTypedArray()
        } else {
            viewModel.getPokemonTypes().toTypedArray()
        }

        return buildDialog(pokemonName, items)
    }

    private fun buildDialog(pokemonName: String, items: Array<String>): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setTitle(if(showAbilities) "Abilities of $pokemonName" else "Types of $pokemonName")
            setItems(items, null)
            setPositiveButton("OK", null)
            create()
        }
    }

    companion object {
        fun newInstance(pokemonName: String, showAbilities: Boolean): PokemonActivityDialog {
            val args = Bundle().apply {
                putString("pokemonName", pokemonName)
            }
            return PokemonActivityDialog().apply {
                arguments = args
                setShowAbilities(showAbilities)
            }
        }
    }
}
