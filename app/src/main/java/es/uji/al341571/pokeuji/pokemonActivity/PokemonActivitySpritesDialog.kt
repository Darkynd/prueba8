package es.uji.al341571.pokeuji.pokemonActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class PokemonActivitySpritesDialog: DialogFragment() {
    private val viewModel: PokemonViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val spritesNames = viewModel.getPokemonSprites().toTypedArray()
        return AlertDialog.Builder(requireContext()).run {
            setTitle("Select a Sprite")
            setItems(spritesNames) { _, which ->
                viewModel.onSpriteSelected(spritesNames[which])
            }
            setPositiveButton("OK", null)
            create()
        }
    }
}