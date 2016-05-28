package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	List<River> rivers;

	public Model() {
		RiversDAO riversDao = new RiversDAO();
		// Get all the data from the DB
		rivers = riversDao.getAllRivers();
		for (River river : rivers) {
			riversDao.getFlows(river);
		}
	}

	public SimulationResult simulate(River river, double k) {
		
		List<Double> capacity = new ArrayList<Double>();
		
		double Q = convertM2SecToM2Day(k * 30 * river.getFlowAvg());
		double C = Q / 2;
		double fOutMin = convertM2SecToM2Day(0.8 * river.getFlowAvg());
		int numberOfDays = 0;

		System.out.println("Q: " + Q);

		// Events are the flows
		for (Flow flow : river.getFlows()) {

			System.out.println("Date: " + flow.getDay());
			
			double fOut = fOutMin;

			// C'è il 5% di possibilità che fOut sia 10 volte fOutMin
			if (Math.random() > 0.95) {
				fOut = 10 * fOutMin;
				System.out.println("10xfOutMin");
			}

			System.out.println("fOut: " + fOut);
			System.out.println("fIn: " + convertM2SecToM2Day(flow.getFlow()));

			// Aggiungo fIn a C
			C += convertM2SecToM2Day(flow.getFlow());

			// Se C è maggiore della capacità massima
			if (C > Q) {
				// Tracimazione
				// La quantità in più esce.
				C = Q;
			}

			if (C < fOut) {
				// Non riesco a garantire la quantità minima.
				numberOfDays++;
				// Il bacino comunque si svuota
				C = 0;
			} else {
				// Faccio uscire la quantità giornaliera
				C -= fOut;
			}

			System.out.println("C: " + C + "'\n");

			// Mantengo un lista della capacità giornaliere del bacino
			capacity.add(C);
		}

		// Calcolo la media della capacità
		double CAvg = 0;
		for (Double d : capacity) {
			CAvg += d;
		}
		CAvg = CAvg / capacity.size();
		System.out.println("Number of days in which it cannot guarantee the minimum capacity: " + numberOfDays);
		System.out.println("Average Capacity: " + CAvg);
		return new SimulationResult(CAvg, numberOfDays);
	}

	public double convertM2SecToM2Day(double input) {
		return input * 60 * 60 * 24;
	}

	public double convertM2DayToM2Sec(double input) {
		return input / 60 / 60 / 24;
	}

	public List<River> getRivers() {
		return rivers;
	}

	public LocalDate getStartDate(River river) {
		if (!river.getFlows().isEmpty()) {
			return river.getFlows().get(0).getDay();
		}
		return null;
	}

	public LocalDate getEndDate(River river) {
		if (!river.getFlows().isEmpty()) {
			return river.getFlows().get(river.getFlows().size() - 1).getDay();
		}
		return null;
	}

	public int getNumMeasurements(River river) {
		return river.getFlows().size();
	}

	public double getFMed(River river) {
		double avg = 0;
		for (Flow f : river.getFlows())
			avg += f.getFlow();
		avg /= river.getFlows().size();
		river.setFlowAvg(avg);
		return avg;
	}

}
