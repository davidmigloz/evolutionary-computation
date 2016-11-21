package com.davazriel.airtafficcontroller.algo;

import com.davazriel.airtafficcontroller.model.Airport;
import com.davazriel.airtafficcontroller.model.Flight;
import net.sf.jclec.IConfigure;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;

import java.util.Comparator;

import net.sf.jclec.orderarray.OrderArrayIndividual;
import org.apache.commons.configuration.Configuration;

public class AirTrafficController extends AbstractEvaluator implements IConfigure {

    private static final boolean MINIMIZE = true;

    private Comparator<IFitness> comparator;

    public AirTrafficController() {
        super();
    }

    @Override
    protected void evaluate(IIndividual ind) {
        // Individual genotype
        int[] genotype = ((OrderArrayIndividual) ind).getGenotype();
        // TODO inicializar airport correctamente
        Airport airport = new Airport(0, null);
        for (int individual : genotype) {
            // TODO inicializar flight correctamente
            Flight flight = new Flight(0, null, null);
            airport.scheduleFlight(flight);
        }
        ind.setFitness(new SimpleValueFitness(airport.getAccumulatedDelay()));
    }

    @Override
    public Comparator<IFitness> getComparator() {
        if (comparator == null) {
            comparator = new ValueFitnessComparator(MINIMIZE);
        }
        return comparator;
    }

    @Override
    public void configure(Configuration conf) {
        // TODO Auto-generated method stub
    }
}
