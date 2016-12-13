package com.davazriel.airtafficcontroller.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davazriel.airtafficcontroller.model.Airport;
import com.davazriel.airtafficcontroller.model.Flight;
import com.davazriel.airtafficcontroller.model.Flight.PlaneType;
import com.davazriel.airtafficcontroller.utils.DataReader;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.fitness.CompositeValueFitness;
import net.sf.jclec.fitness.ParetoComparator;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;
import net.sf.jclec.orderarray.OrderArrayIndividual;

/**
 * Clase que extiende AbstractEvaluator (para que tenga el metodo evaluate) e
 * implementa la interfaz IConfigure (para que tenga el metodo configure).
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
	private ParetoComparator comparator;

	/**
	 * Archivo donde estan los vuelos.
	 */
	private String flightsFile;

	/**
	 * Archivo donde estan los tiempos de espera.
	 */
	private String waitTimesFile;

	/**
	 * Archivo donde estan las restricciones de cada pista.
	 */
	private String runwayRestrictionsFile;

	/**
	 * Archivo donde estan las restricciones de que avion tiene que ir detras de
	 * cual otro
	 */
	private String contiguousFlightsFile;

	/**
	 * Número de pistas.
	 */
	private int nRunways;

	/**
	 * Margen de tiempo entre aterrizajes dependiendo del tipo de avión.
	 */
	private int[][] waitingTimes;

	/**
	 * Vuelos que recibimos.
	 */
	private Map<String, Flight> flights;

	/**
	 * Mapa donde se guardan las restricciones de pista como <id
	 * pista>-><PlaneTypes no admitidos>
	 */
	private Map<Integer, List<PlaneType>> runwayRest;

	/**
	 * Constructor, inicializamos la lista para los vuelos.
	 */
	public AirTrafficController() {
		super();
	}

	/**
	 * Recibe un individuo y calcula su valor de fitness.
	 * 
	 * @param ind
	 *            Individuo que será evaluado.
	 */
	@Override
	protected void evaluate(IIndividual ind) {
		// Individual genotype
		int[] genotype = ((OrderArrayIndividual) ind).getGenotype();
		// Create airport
		Airport airport = new Airport(nRunways, waitingTimes, runwayRest);
		// Schedule flights: Genotype order = arrival order; genotipe values =
		// flight number
		List<Flight> flightsList = new ArrayList<>(flights.values());
		for (int vuelo : genotype) {
			Flight flight = flightsList.get(vuelo);
			airport.scheduleFlight(flight);
		}
		SimpleValueFitness[] fitness = {
		new SimpleValueFitness(airport.getAccumulatedDelay()),
		new SimpleValueFitness(airport.getAccumulatedRunwayRestrictionViolations()),
		new SimpleValueFitness(airport.getAccumulatedConsecutiveFlightRestriction())};
		// Calculate fitness
		ind.setFitness(new CompositeValueFitness(fitness));
		// Log last landing
		logger.debug(Arrays.toString(genotype) + ":" + airport.getMaxATA());
	}

	/**
	 * El comparador será singleton.
	 */
	@Override
	public Comparator<IFitness> getComparator() {
		if (comparator == null) {
			Comparator<IFitness> comparators[] = new Comparator[3];
			comparators[0] = new ValueFitnessComparator(MINIMIZE);
			comparators[1] = new ValueFitnessComparator(MINIMIZE);
			comparators[2] = new ValueFitnessComparator(MINIMIZE);
			
			comparator = new ParetoComparator();
			comparator.setComponentComparators(comparators);
		}
		return comparator;
	}

	/**
	 * Metodo para configurar nuestro evaluador.
	 * 
	 * @param conf
	 *            configuración.
	 */
	@Override
	public void configure(Configuration conf) {
		this.waitTimesFile = conf.getString("[@wait-times-file]");
		this.flightsFile = conf.getString("[@flights-file]");
		this.runwayRestrictionsFile = conf.getString("[@runway-file]");
		this.contiguousFlightsFile = conf.getString("[@consecutive-flights-file]");

		DataReader dataReader = new DataReader();

		// Parse waiting times
		dataReader.openFile(waitTimesFile);
		waitingTimes = dataReader.readMatrix(3, 3);
		dataReader.closeFile();

		// Parse flights
		flights = new LinkedHashMap<>();
		String[] flightString = null;
		dataReader.openFile(flightsFile);
		while (dataReader.ready()) {
			flightString = dataReader.readLine();
			int[] runwayETAs = new int[flightString.length - 2];
			for (int i = 2; i < flightString.length; i++) {
				runwayETAs[i - 2] = Integer.valueOf(flightString[i]);
			}
			flights.put(flightString[0], new Flight(flightString[0], PlaneType.valueOf(flightString[1]), runwayETAs));
		}
		dataReader.closeFile();

		// Parse runway restrictions
		runwayRest = new HashMap<>();
		dataReader.openFile(runwayRestrictionsFile);
		while (dataReader.ready()) {
			String[] runwayRestString = dataReader.readLine();
			Integer runway = Integer.valueOf(runwayRestString[0]);
			List<PlaneType> notAllowed = new ArrayList<>();
			for (int i = 1; i < runwayRestString.length; i++) {
				notAllowed.add(PlaneType.valueOf(runwayRestString[i]));
			}
			runwayRest.put(runway, notAllowed);
		}
		dataReader.closeFile();

		// Parse contiguous flights restrictions
		Map<String, String> contiguousFlightsRest = new HashMap<>();
		dataReader.openFile(contiguousFlightsFile);
		while (dataReader.ready()) {
			String[] consFlightsString = dataReader.readLine();
			int i = 0;
			do {
				contiguousFlightsRest.put(consFlightsString[i], consFlightsString[(i++)]);
			} while (i < consFlightsString.length - 1);
		}
		dataReader.closeFile();

		// Add contiguous flights restrictions to flight objects
		for(String flightId : contiguousFlightsRest.keySet()) {
			Flight contiguousFlight = flights.get(contiguousFlightsRest.get(flightId));
			flights.get(flightId).setContiguousFlight(contiguousFlight);
		}

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
	public String getWaitTimesFile() {
		return waitTimesFile;
	}

	@SuppressWarnings("unused")
	public void setWaitTimesFile(String waitTimesFile) {
		this.waitTimesFile = waitTimesFile;
	}

	@SuppressWarnings("unused")
	public String getRunwayRestrictionsFile() {
		return runwayRestrictionsFile;
	}

	@SuppressWarnings("unused")
	public void setRunwayRestrictionsFile(String runwayRestrictionsFile) {
		this.runwayRestrictionsFile = runwayRestrictionsFile;
	}

	@SuppressWarnings("unused")
	public String getContiguousFlightsFile() {
		return contiguousFlightsFile;
	}

	@SuppressWarnings("unused")
	public void setContiguousFlightsFile(String contiguousFlightsFile) {
		this.contiguousFlightsFile = contiguousFlightsFile;
	}
}
