package com.evolutiongenerator.model.mapObject.Animal;

import com.evolutiongenerator.constant.AnimalBehaviourVariant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenesTest {

    @Test
    public void testGetGenesSize(){
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(5);
        genes.add(2);
        genes.add(1);
        genes.add(3);
        genes.add(7);
        genes.add(1);
        genes.add(3);
        genes.add(3);
        genes.add(7);

        Genes gene = new Genes(genes, 0, 0, null, null);
        assertEquals(10, gene.getGenesSize());
    }

    @Test
    public void testGetCurrentGen(){
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(5);
        genes.add(2);
        genes.add(1);
        genes.add(3);
        genes.add(7);
        genes.add(1);
        genes.add(3);
        genes.add(3);
        genes.add(7);

        Genes gene = new Genes(genes, 0,0, null, AnimalBehaviourVariant.NORMAL);
        gene.getGen();
        assertEquals(1, gene.getCurrentGen());
    }

    @Test
    public void testToString(){
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(5);
        genes.add(2);
        genes.add(1);
        genes.add(3);
        genes.add(7);
        genes.add(1);
        genes.add(3);
        genes.add(3);
        genes.add(7);

        Genes gene = new Genes(genes, 0, 0, null, AnimalBehaviourVariant.NORMAL);
        assertEquals("1521371337", gene.toString());
    }
}
