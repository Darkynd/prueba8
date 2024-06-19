package es.uji.al341571.pokeuji.evolutionActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.pokeuji.databinding.RecyclerviewEvolutionItemBinding
import java.util.concurrent.atomic.AtomicInteger

class EvolutionChainTreeAdapter(
    private val rootNode: EvolutionChainTree,
    private val onNodeClicked: (EvolutionChainTree) -> Unit
) : RecyclerView.Adapter<EvolutionChainTreeAdapter.NodeViewHolder>() {

    private var counter: Int = 0
    private val nNodes: Int = rootNode.totalNodes

    inner class NodeViewHolder(private val binding: RecyclerviewEvolutionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(node: EvolutionChainTree) {
            val formattedName = formatNodeName(node)
            binding.nodeEvolutionDisplay.text = formattedName
            binding.root.setOnClickListener {
                onNodeClicked(node)
            }
        }

        private fun formatNodeName(node: EvolutionChainTree): String {
            counter++
            val depth = calculateNodeDepth(node)
            val isLast = counter >= nNodes
            val branchPrefix: String
            val trunkPrefix: String

            return if(node.isRoot()) {
                node.speciesName
            } else if (depth <= 1) {
                branchPrefix = if(node.parent?.childrenNodesList?.last() != node) "├──" else "└──"
                "$branchPrefix${node.speciesName}"
            } else {
                val indentation = "\t".repeat(depth*3)
                branchPrefix = if(node.parent?.childrenNodesList?.last() != node) "├──" else "└──"
                trunkPrefix = if(node.parent?.childrenNodesList?.last() == node && !isLast) "│" else ""
                "$trunkPrefix$indentation$branchPrefix ${node.speciesName}"
            }
        }

        private fun calculateNodeDepth(node: EvolutionChainTree): Int {
            var depth = 0
            var currentNode = node
            while (!currentNode.isRoot()) {
                depth++
                currentNode = currentNode.parent ?: break
            }
            return depth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewEvolutionItemBinding.inflate(inflater, parent, false)
        return NodeViewHolder(binding)
    }

    // Gets the node at the specified position
    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val currentNode = getNodeAtPosition(rootNode, position)
        currentNode?.let {
            holder.bind(it)
        }
    }

    // Returns the total number of nodes in the tree
    override fun getItemCount(): Int {
        return countNodes(rootNode)
    }

    private fun countNodes(node: EvolutionChainTree?): Int {
        if (node == null) return 0
        var count = 1
        for (childNode in node.childrenNodesList) {
            count += countNodes(childNode)
        }
        return count
    }

    private fun getNodeAtPosition(node: EvolutionChainTree, position: Int): EvolutionChainTree? {
        val index = AtomicInteger(0)
        return getNodeAtPositionRecursive(node, position, index)
    }

    private fun getNodeAtPositionRecursive(node: EvolutionChainTree, position: Int, index: AtomicInteger): EvolutionChainTree? {
        if (index.get() == position) {
            return node
        }
        for (childNode in node.childrenNodesList) {
            index.incrementAndGet()
            val result = getNodeAtPositionRecursive(childNode, position, index)
            if (result != null) {
                return result
            }
        }
        return null
    }
}