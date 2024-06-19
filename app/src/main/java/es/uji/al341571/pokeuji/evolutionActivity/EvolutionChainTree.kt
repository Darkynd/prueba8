package es.uji.al341571.pokeuji.evolutionActivity

class EvolutionChainTree(val speciesName: String) {
    val childrenNodesList = mutableListOf<EvolutionChainTree>()
    var parent: EvolutionChainTree? = null // Reference to the parent node
    var totalNodes: Int = 1 // Total nodes number counter, initialized to 1 for the current node

    fun addChildren(evolutionChainTree: EvolutionChainTree) {
        evolutionChainTree.parent = this // Set the current node as the parent of the new node
        childrenNodesList.add(evolutionChainTree)
        updateTotalNodes()
    }

    private fun updateTotalNodes() {
        totalNodes = 1 // Reset the counter
        for (childNode in childrenNodesList) {
            totalNodes += childNode.calculateTotalNodes() // Add nodes of the children
        }
        parent?.updateTotalNodes() // Update the counter in the parent if it exists
    }

    private fun calculateTotalNodes(): Int {
        var count = 1 // Counter for the current node
        for (childNode in childrenNodesList) {
            count += childNode.calculateTotalNodes() // Recursively adds the child nodes
        }
        return count
    }

    fun isRoot(): Boolean {
        return parent == null
    }
}

fun generateEvolutionChainTree(evolutionChain: EvolutionChain): EvolutionChainTree {
    val originalSpecies = EvolutionChainTree(evolutionChain.evolution_chain.chain.species.name)
    evolutionChain.evolution_chain.chain.evolves_to?.forEach { evolvesToResponse ->
        buildEvolutionChainTree(originalSpecies, evolvesToResponse)
    }
    return originalSpecies
}

private fun buildEvolutionChainTree(parent: EvolutionChainTree, evolvesToResponse: EvolvesToResponse) {
    val childNode = EvolutionChainTree(evolvesToResponse.species?.name ?: "")
    parent.addChildren(childNode)
    evolvesToResponse.evolves_to?.forEach { subEvolvesToResponse ->
        buildEvolutionChainTree(childNode, subEvolvesToResponse)
    }
}