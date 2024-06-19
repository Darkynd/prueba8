package es.uji.al341571.pokeuji.speciesActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.pokeuji.databinding.RecyclerviewItemBinding

class VarietyAdapter(
    private val varietyList: List<Variety>,
    val onItemClick: (Variety)-> Unit)
    : RecyclerView.Adapter<VarietyAdapter.VarietyViewHolder>() {

    inner class VarietyViewHolder(private val itemBinding: RecyclerviewItemBinding):
        RecyclerView.ViewHolder(itemBinding.root) {
            fun bindItem(variety: Variety) {
                itemBinding.varietyName.text = variety.name

                if (variety.isDefault) {
                    itemBinding.catchingPokemonIcon.visibility = View.VISIBLE
                } else {
                    itemBinding.catchingPokemonIcon.visibility = View.GONE
                }

                itemBinding.root.setOnClickListener {
                    onItemClick(variety)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarietyViewHolder {
        return VarietyViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VarietyViewHolder, position: Int) {
        val variety = varietyList[position]
        holder.bindItem(variety)
    }

    override fun getItemCount(): Int {
        return varietyList.size
    }
}