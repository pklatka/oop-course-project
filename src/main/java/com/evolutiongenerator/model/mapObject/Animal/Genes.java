package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.constant.AnimalBehaviourVariant;
import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.utils.Randomize;

import java.util.ArrayList;
import java.util.List;

/**
 * A class describing the field in the animal class used for the process of movement and reproduction
 *
 * @author Paweł Motyka
 */
public class Genes {
    private final List<Integer> genesList;
    private int currentGenIndex;
    private final int maxMutationAmount;
    private final int minMutationAmount;
    private final MutationVariant mutationVariant;
    private final AnimalBehaviourVariant behaviourVariant;
    private int currentGen = -1;

    public Genes(int numberOfGenes, int maxMutationAmount, int minMutationAmount, MutationVariant mutationVariant, AnimalBehaviourVariant behaviourVariant) {
        this.maxMutationAmount = maxMutationAmount;
        this.minMutationAmount = minMutationAmount;
        genesList = new ArrayList<>();
        this.mutationVariant = mutationVariant;
        this.behaviourVariant = behaviourVariant;
        generateGenes(numberOfGenes);
        currentGenIndex = Randomize.generateInt(numberOfGenes - 1, 0);
        currentGen = this.genesList.get(currentGenIndex);

    }

    public Genes(List<Integer> genes, int maxMutationAmount, int minMutationAmount, MutationVariant mutationVariant, AnimalBehaviourVariant behaviourVariant) {
        genesList = genes;
        this.mutationVariant = mutationVariant;
        this.maxMutationAmount = maxMutationAmount;
        this.minMutationAmount = minMutationAmount;
        this.behaviourVariant = behaviourVariant;
        mutateGene();
    }

    /**
     * Generate randomly genes
     *
     * @param genesAmount number of genes to generate
     */
    public void generateGenes(int genesAmount) {
        for (int i = 0; i < genesAmount; i++) {
            this.genesList.add(Randomize.generateInt(7, 0));
        }
    }

    public int getGenesSize() {
        return genesList.size();
    }

    /**
     * Generate offspring's genes
     *
     * @param genesAmount number of genes to return
     * @param isLeftSide  from which side to inherit genes
     * @return A slice of the parent's genes for the offspring
     */
    public List<Integer> getOffspringGenes(int genesAmount, boolean isLeftSide) {
        List<Integer> offspringGenes = new ArrayList<>();

        if (isLeftSide) {
            for (int i = 0; i < genesAmount; i++) {
                offspringGenes.add(this.genesList.get(i));
            }
        } else {
            for (int i = this.genesList.size() - genesAmount; i < this.genesList.size(); i++) {
                offspringGenes.add(this.genesList.get(i));
            }
        }
        return offspringGenes;
    }

    public Genes createOffspringGenes(List<Integer> genesList) {
        return new Genes(genesList, maxMutationAmount, minMutationAmount, mutationVariant, behaviourVariant);
    }

    /**
     * Get currently active gene
     *
     * @return currently active gene
     */
    public int getCurrentGen() {
        return currentGen;
    }


    /**
     * Returns the animal's current gene with consideration of how the animal behaves
     * If we are on the last gen the next will be the first in the array
     *
     * @return Current gen in order.
     */
    public int getGen() {
        boolean isNormal = Randomize.generateBooleanWithProbability(0.8);
        if (behaviourVariant == AnimalBehaviourVariant.NORMAL || (behaviourVariant == AnimalBehaviourVariant.A_BIT_OF_MADNESS && isNormal)) {
            if (this.currentGenIndex == this.genesList.size()) {
                this.currentGenIndex = 0;
            }
            int currentGen = this.genesList.get(this.currentGenIndex);
            this.currentGenIndex += 1;
            this.currentGen = currentGen;
            return currentGen;
        }

        int currnetGen = currentGenIndex;
        while (currnetGen == currentGenIndex) {
            currnetGen = Randomize.generateInt(this.genesList.size(), 0);
        }
        return currnetGen;
    }

    /**
     * Mutation of genes depending on the selected mutation variant.
     */
    private void mutateGene() {
        int noOfGenesToMutate = Randomize.generateInt(this.maxMutationAmount, this.minMutationAmount);
        for (int i = 0; i < noOfGenesToMutate; i++) {
            int geneIndexToMutate = Randomize.generateInt(this.genesList.size() - 1, 0);

            if (this.mutationVariant == MutationVariant.RANDOM) {
                this.genesList.set(geneIndexToMutate, Randomize.generateInt(7, 0));
            } else {
                int mutate = Randomize.generateBoolean() ? 1 : -1;
                int currentGenValue = this.genesList.get(geneIndexToMutate);
                int newValue = currentGenValue + mutate < 0 ? 7 : (currentGenValue + mutate) % 8;
                this.genesList.set(geneIndexToMutate, newValue);
            }
        }
    }

    @Override
    public String toString() {
        return genesList.stream().map(String::valueOf).reduce("", (a, b) -> a + b);
    }
}
