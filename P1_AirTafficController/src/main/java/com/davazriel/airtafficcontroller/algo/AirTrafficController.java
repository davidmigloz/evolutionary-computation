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

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


/**
 * Clase que extiende AbstractEvaluator (para que tenga el metodo evaluate) e implementa
 * la interfaz IConfigure (para que tenga el metodo configure).
 * 
 * @author David Miguel 
 * @author Javier Martínez
 *
 */
public class AirTrafficController extends AbstractEvaluator implements IConfigure {

	// Auto generated by eclipse.
	private static final long serialVersionUID = -6267408581135583015L;
	
	/**
	 * Estamos minimizando por lo que es MINIMIZE true
	 */
	private static final boolean MINIMIZE = true;

	/**
	 * Comparador para las fitness.
	 */
	private Comparator<IFitness> comparator;
	
	/**
	 * Numero de pistas
	 */
	private int n_runways;
	/**
	 * Tiempos que tarda cada avion de cada tipo en poder aterrizar despues de que 
	 * lo haya hecho otro antes.
	 */
	private int[][] waitTimes;

	/**
	 * Vuelos que recibimos.
	 */
	private List<Flight> flights;
	
	/**
	 * Archivo donde estan los vuelos
	 */
	private String flightsFile;
	/**
	 * Archivo donde estan los tiempos de espera
	 */
	private String waitTimesFileName;

	/**
	 * Constructor, inicializamos la lista para los vuelos
	 */
	public AirTrafficController() {
		super();
		flights = new ArrayList<>();
	}

	/**
	 * Evaluate, recibiendo un individuo ind cambiamos el valor de fitness del 
	 * mismo.
	 * @param ind Individuo que sera evaluado
	 */
	@Override
	protected void evaluate(IIndividual ind) {
		// Individual genotype
		int[] genotype = ((OrderArrayIndividual) ind).getGenotype();
		Airport airport = new Airport(n_runways, waitTimes);
		Flight[] scheduleFlights = new Flight[genotype.length];
		for (int i = 0; i < genotype.length; i++) {
			scheduleFlights[genotype[i]] = flights.get(i);
		}
		for (Flight scheduleFlight : scheduleFlights) {
			airport.scheduleFlight(scheduleFlight);
		}
		ind.setFitness(new SimpleValueFitness(airport.getMaxATA()));
	}

	/**
	 * El comparador será singleton
	 */
	@Override
	public Comparator<IFitness> getComparator() {
		if (comparator == null) {
			comparator = new ValueFitnessComparator(MINIMIZE);
		}
		return comparator;
	}

	/**
	 * Metodo para configurar nuestro evaluador
	 */
	@Override
	public void configure(Configuration conf) {
		this.waitTimesFileName = conf.getString("[@wait-times-file]");
		this.flightsFile = conf.getString("[@flights-file]");
		String[] flightString = null;
		
		DataReader waitTimesDataReader = new DataReader();
		waitTimesDataReader.openFile(waitTimesFileName);
		waitTimes = waitTimesDataReader.readMatrix(3, 3);
		waitTimesDataReader.closeFile();

		DataReader flightsDataReader = new DataReader();
		flightsDataReader.openFile(flightsFile);
		while (flightsDataReader.ready()) {
			flightString = flightsDataReader.readLine();
			int[] runwayETAs = new int[flightString.length - 2];
			for (int i = 2; i < flightString.length; i++) {
				runwayETAs[i - 2] = Integer.valueOf(flightString[i]);
			}
			flights.add(new Flight(flightString[0], Flight.PlaneType.valueOf(flightString[1]), runwayETAs));
		}
		flightsDataReader.closeFile();

		n_runways = flightString.length - 2;
	}
	
	public String getFlightsFile() {
		return flightsFile;
	}

	public void setFlightsFile(String flightsFile) {
		this.flightsFile = flightsFile;
	}

	public String getWaitTimesFileName() {
		return waitTimesFileName;
	}

	public void setWaitTimesFileName(String waitTimesFileName) {
		this.waitTimesFileName = waitTimesFileName;
	}
}
