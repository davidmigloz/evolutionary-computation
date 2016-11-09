package com.davazriel.airtafficcontroller.algo;

import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.binarray.BinArrayIndividual;

import java.util.Comparator;

public class AirRafficController extends AbstractEvaluator {

    @Override
    protected void evaluate(IIndividual ind) {
        // Individual genotype
        byte[] genotype = ((BinArrayIndividual) ind).getGenotype();

    }

    @Override
    public Comparator<IFitness> getComparator() {
        return null;
    }
}
