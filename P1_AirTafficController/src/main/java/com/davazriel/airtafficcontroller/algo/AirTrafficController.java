package com.davazriel.airtafficcontroller.algo;

import com.davazriel.airtafficcontroller.model.Airport;
import com.davazriel.airtafficcontroller.model.Flight;
import com.davazriel.airtafficcontroller.utils.DataReader;
import net.sf.jclec.IConfigure;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;
import net.sf.jclec.orderarray.OrderArrayIndividual;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


/**
 * Clase que extiende AbstractEvaluator (para que tenga el metodo evaluate) e implementa
 * la interfaz IConfigure (para que tenga el metodo configure).
 *
 * @author David Miguel
 * @author Javier Martínez
 */
@SuppressWarnings("ConstantConditions")
public class AirTrafficController extends AbstractEvaluator implements IConfigure {

    private static final Logger logger = LoggerFactory.getLogger(AirTrafficController.class);
    private static final long serialVersionUID = -6267408581135583015L;

    /**
     * Estamos minimizando por lo que es MINIMIZE true.
     */
    private static final boolean MINIMIZE = true;

    /**
     * Comparador para las fitness.
     */
    private Comparator<IFitness> comparator;

    /**
     * Archivo donde estan los vuelos.
     */
    private String flightsFile;

    /**
     * Archivo donde estan los tiempos de espera.
     */
    private String waitTimesFileName;

    /**
     * Número de pistas.
     */
    private int nRunways;

    /**
     * Margen de tiempo entre aterrizajes dependiendo del tipo de avión.
     */
    private int[][] waitTimes;

    /**
     * Vuelos que recibimos.
     */
    private List<Flight> flights;

    /**
     * Constructor, inicializamos la lista para los vuelos.
     */
    public AirTrafficController() {
        super();
        flights = new ArrayList<>();
    }

    /**
     * Recibe un individuo y calcula su valor de fitness.
     * @param ind Individuo que será evaluado.
     */
    @Override
    protected void evaluate(IIndividual ind) {
        // Individual genotype
        int[] genotype = ((OrderArrayIndividual) ind).getGenotype();
        // Create airport
        Airport airport = new Airport(nRunways, waitTimes);
        // Schedule flights: Genotype order = arrival order; genotipe values = flight number
        for (int vuelo : genotype) {
            Flight flight = flights.get(vuelo);
            airport.scheduleFlight(flight);
        }
        // Calculate fitness
        ind.setFitness(new SimpleValueFitness(airport.getAccumulatedDelay()));
        // Log last landing
        logger.debug(Arrays.toString(genotype) + ":" + airport.getMaxATA());
    }

    /**
     * El comparador será singleton.
     */
    @Override
    public Comparator<IFitness> getComparator() {
        if (comparator == null) {
            comparator = new ValueFitnessComparator(MINIMIZE);
        }
        return comparator;
    }

    /**
     * Metodo para configurar nuestro evaluador.
     * @param conf configuración.
     */
    @Override
    public void configure(Configuration conf) {
        this.waitTimesFileName = conf.getString("[@wait-times-file]");
        this.flightsFile = conf.getString("[@flights-file]");
        String[] flightString = null;

        DataReader dataReader = new DataReader();
        dataReader.openFile(waitTimesFileName);
        waitTimes = dataReader.readMatrix(3, 3);
        dataReader.closeFile();

        dataReader.openFile(flightsFile);
        while (dataReader.ready()) {
            flightString = dataReader.readLine();
            int[] runwayETAs = new int[flightString.length - 2];
            for (int i = 2; i < flightString.length; i++) {
                runwayETAs[i - 2] = Integer.valueOf(flightString[i]);
            }
            flights.add(new Flight(flightString[0], Flight.PlaneType.valueOf(flightString[1]), runwayETAs));
        }
        dataReader.closeFile();

        nRunways = flightString.length - 2;
    }

    // GETTERS Y SETTERS para el VisualIJCLEC

    @SuppressWarnings("unused")
    public String getFlightsFile() {
        return flightsFile;
    }

    @SuppressWarnings("unused")
    public void setFlightsFile(String flightsFile) {
        this.flightsFile = flightsFile;
    }

    @SuppressWarnings("unused")
    public String getWaitTimesFileName() {
        return waitTimesFileName;
    }

    @SuppressWarnings("unused")
    public void setWaitTimesFileName(String waitTimesFileName) {
        this.waitTimesFileName = waitTimesFileName;
    }
}
