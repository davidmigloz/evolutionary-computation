package com.davazriel.airtafficcontroller.algo;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sf.jclec.orderarray.OrderArrayIndividual;
import org.apache.commons.configuration.Configuration;

public class AirTrafficController extends AbstractEvaluator implements IConfigure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean maximize = false;

	private int wait_times[][];

	private int flight_times[][];
	private int plane_types[];

	private int number_of_tracks;

	private Comparator<IFitness> COMPARATOR;

	public AirTrafficController() {
		super();
	}

	@Override
	protected void evaluate(IIndividual ind) {
		// Individual genotype
		int[] genotype = ((OrderArrayIndividual) ind).getGenotype();
		Airport airport = new Airport();

		for (int arrival = 0; arrival < genotype.length; ++arrival) {
			Plane plane = new Plane(plane_types[arrival], wait_times[arrival]);
			airport.addArrival(plane);
		}

		ind.setFitness(new SimpleValueFitness(airport.lastFreeTrack()));
	}

	private class Plane {
		private int type;
		private int[] arrivals;

		public Plane(int type, int[] arrivals) {
			this.type = type;
			this.arrivals = arrivals;
		}

		public int getArrivalForTrack(int trackNumber) {
			return arrivals[trackNumber];
		}

		public int getType() {
			return type;
		}
	}

	private class Airport {
		private List<LandingTrack> tracks;

		public Airport() {
			tracks = new ArrayList<>();
			for (int i = 0; i < number_of_tracks; ++i) {
				tracks.add(new LandingTrack());
			}
		}

		public void addArrival(Plane plane) {
			int track = nextFreeTrack(plane.getType());
			tracks.get(track).addLanding(plane.type, plane.getArrivalForTrack(track));
		}

		private int nextFreeTrack(int planeType) {
			int best = 0;
			int bestTime = tracks.get(0).getMinLandingTime(planeType);
			for (int i = 1; i < tracks.size(); ++i) {
				int thisTime = tracks.get(i).getMinLandingTime(planeType);
				if (thisTime < bestTime) {
					best = i;
					bestTime = thisTime;
				}
			}
			return best;
		}

		public int lastFreeTrack() {
			int worst = 0;
			int worstTime = tracks.get(0).getLastTime();
			for (int i = 1; i < tracks.size(); ++i) {
				int thisTime = tracks.get(i).getLastTime();
				if (thisTime > worstTime) {
					worst = i;
					worstTime = thisTime;
				}
			}
			return worst;
		}

	}

	private class LandingTrack {
		private int nextLanding;
		private int currentPlaneType;

		public LandingTrack() {
			nextLanding = 0;
			currentPlaneType = -1;
		}

		public int getLastTime() {
			return nextLanding;
		}

		public int getMinLandingTime(int planeType) {
			if (currentPlaneType == -1) {
				return nextLanding;
			} else {
				return nextLanding + wait_times[currentPlaneType][planeType];
			}
		}

		public void addLanding(int planeType, int landingTime) {
			int minNextTime = getMinLandingTime(planeType);
			if (landingTime < minNextTime) {
				nextLanding = minNextTime;
			} else {
				nextLanding = landingTime;
			}
			currentPlaneType = planeType;
		}
	}

	@Override
	public Comparator<IFitness> getComparator() {
		if (COMPARATOR == null)
			COMPARATOR = new ValueFitnessComparator(!maximize);

		return COMPARATOR;
	}

	@Override
	public void configure(Configuration conf) {
		// TODO Auto-generated method stub

	}
}
