package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.utils.Randomize;

import java.util.ArrayList;
/**
 * @author Paweł Motyka
 */
public class Genes {
    private final ArrayList<Integer> genesList;
    private int currentGenIndex = 0;
    private final int maxMutationAmount;
    private final int minMutationAmount;
    private final MutationVariant mutationVariant;

    public Genes(int numberOfGenes, int maxMutationAmount, int minMutationAmount, MutationVariant mutationVariant) {
        this.maxMutationAmount = maxMutationAmount;
        this.minMutationAmount = minMutationAmount;
        this.genesList = new ArrayList<>();
        this.generateGenes(numberOfGenes);
        this.mutationVariant = mutationVariant;
    }

    public Genes(ArrayList<Integer> genes, int maxMutationAmount, int minMutationAmount, MutationVariant mutationVariant){
        this.genesList = genes;
        this.mutationVariant = mutationVariant;
        this.maxMutationAmount = maxMutationAmount;
        this.minMutationAmount = minMutationAmount;
        this.mutateGene();
    }

    /**
     *  Generate randomly genes
     * @param genesAmount number of genes to generate
     */
    public void generateGenes(int genesAmount){
        for (int i = 0; i < genesAmount; i++) {
            this.genesList.add(Randomize.generateInt(8, 0));
        }
    }

    /**
     *
     * @param genesAmount number of genes to return
     * @param isLeftSide from which side to inherit genes
     * @return A slice of the parent's genes for the offspring
     */
    public ArrayList<Integer> getOffspringGenes(int genesAmount, boolean isLeftSide){
        ArrayList<Integer> offspringGenes = new ArrayList<>();

        if (isLeftSide){
            for (int i = 0; i < genesAmount; i++) {
                offspringGenes.add(this.genesList.get(i));
            }
        }else {
            for (int i = this.genesList.size() - genesAmount ; i < this.genesList.size(); i++) {
                offspringGenes.add(this.genesList.get(i));
            }
        }
    return offspringGenes;
    }


    /**
     * If we are on the last gen the next will be the first in the array
     * It returns current gen and move currentIndex to next
     *
     * @return Current gen in order.
     */
    public int getGen() {
        if (this.currentGenIndex == this.genesList.size()){
            this.currentGenIndex = 0;
        }
        int currentGen = this.genesList.get(this.currentGenIndex);
        this.currentGenIndex += 1;
        return currentGen;
    }

    /**
     * Mutation of genes depending on the selected mutation variant.
     */
    private void mutateGene(){
        int noOfGenesToMutate = Randomize.generateInt(this.maxMutationAmount,this.minMutationAmount);

        for (int i = 0; i < noOfGenesToMutate; i++) {
            int geneIndexToMutate = Randomize.generateInt(this.genesList.size() - 1,0);

            if (this.mutationVariant == MutationVariant.RANDOM){
                this.genesList.set(geneIndexToMutate,Randomize.generateInt(7,0));
            }else {
                int mutate = Randomize.generateBoolean() ? 1 : -1;
                int currentGenValue = this.genesList.get(geneIndexToMutate);
                int newValue =  currentGenValue + mutate < 0 ? 7 : (currentGenValue + mutate) % 8;
                this.genesList.set(geneIndexToMutate, newValue);
            }
        }


    }
}
