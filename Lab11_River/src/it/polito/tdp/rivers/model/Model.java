package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	List<River> rivers=null;
	Queue<Event> eventList = new PriorityQueue<Event>();
	
	

	public List<River> getAllRivers() {
		// TODO Auto-generated method stub
		if(rivers==null){
			RiversDAO dao = new RiversDAO();
			rivers = dao.getAllRivers();
			
			for(River el : rivers)
				el.setFlows(dao.getFlowsByRiver(el));
		}
		return rivers;
	}

	/*
	 * OSS: Io all'inizio avevo interrogato il DB per sapere la data iniziale,la finale, il
	 * flusso medio e il numero di misure, ma in realt� non va bene perch� non posso interrogare 
	 * il DB cos� tante volte visto che ho gi� letto tutti i dati e li ho messi nella lista rivers!
	 * Allora un'idea eficiente � quella di implementare in Flow un ordinamento per data, cos� so
	 * gi� per ogni fiume River, che nella sua lista di flows il primo ha data minima e l'ultimo
	 * ha data massima, senza dover fare ricerche!!! Ecco perch� al dao.getFlowsByRiver(el) faccio
	 * restituire la List di flows con i vari flows ordinati
	 */
	public LocalDate getStartDate(River r) {
		// TODO Auto-generated method stub
		if(!r.getFlows().isEmpty())
			return r.getFlows().get(0).getDay();
		
		return null;
	}

	public LocalDate getEndDate(River r) {
		// TODO Auto-generated method stub
		if(!r.getFlows().isEmpty())
			return r.getFlows().get(r.getFlows().size() - 1).getDay();
		
		return null;
	}

	public int getNumMeasurements(River r) {
		// TODO Auto-generated method stub
		return r.getFlows().size();//se � empty allora size � 0
	}

	public double getFMed(River r) {
		// TODO Auto-generated method stub
		double avg = 0;
		for (Flow f : r.getFlows())
			avg += f.getFlow();
		//ora divido avg per il numero di misurazioni fatte
		if(getNumMeasurements(r)!=0)
			avg /= getNumMeasurements(r);
		//altrimenti non faccio nulla e lascio il valore iniziale di avg, ossia 0

		return avg;
	}

	public SimulationResult simula(River r, double K) {
		// TODO Auto-generated method stub
		double Q = K*getFMed(r)*30*24*60*60; //capienza totale, espressa in metri cubi
		double C = Q/2.0; //quantit� presente nel bacino nel giorno attuale
		double fOutMin = 0.8*getFMed(r)*24*60*60; //flusso di uscita minimo al giorno
		int numGiorniOff=0; //numero di giorni in cui non � stata garantita la foutmin
		
		//lista della quantit� di acqua giornaliera presente nel bacino
		List<Double> quantityPerDay = new LinkedList<>(); 		
		
		//pulisco la lista di eventi
		eventList.clear();
		//popolo la lista di eventi
		for(Flow f: r.getFlows())
			eventList.add(new Event(f.getDay(),f));
		
		Event e;
		while((e=eventList.poll())!=null){
			double fIn = e.getFlow().getFlow()*24*60*60; //flusso in ingresso in un dato giorno
			double fOut; //flusso che devo far uscire in un dato giorno 
			// C'� il 5% di probabilit� che fOut sia 10 volte fOutMin
			if(Math.random()<=0.5)
				fOut=10*fOutMin;
			else
				fOut=fOutMin;
			
			//ATT:Ricorda che la C � la quantit� al giorno,NON al secondo! Qui per� non
			//devo moltiplicare per 24*60*60 perch� fIn � gi� espressa al giorno
			C=C+fIn; // Aumento C di fIn
			
			// Se ho avuto un flusso di ingresso eccessivo
			if (C > Q) {
			// Tracimazione
			// La quantit� in pi� esce
			C = Q;
			}
			
			if (C < fOut) {
				// Non riesco a garantire il flusso di uscita fOut richiesto
				numGiorniOff++;
				// ATT: Il bacino comunque si svuota!
				C = 0;
			} else {
				// Faccio uscire la quantit� giornaliera richiesta
				C -= fOut;
			}
			
			// Mantengo un lista della quantit� di acqua giornaliera presente nel bacino
			// alla fine di 
			quantityPerDay.add(Double.valueOf(C));
		}
		
		// Calcolo la media della quantit� C al giorno
		double CAvg = 0;
		for (Double d : quantityPerDay) {
			CAvg += d;
		}
		CAvg = CAvg / quantityPerDay.size();
		
		//ho definito una classe SimulationResult proprio per poter resituire
		//nell' output tutte le info necessarie
		return new SimulationResult(Q, CAvg, numGiorniOff,quantityPerDay.size());
	}
	
	/*
	 * OSS:
	 * La soluzione proposta dal prof non crea una eventList n� tantomeno una classe Event in
	 * quanto alla fine gli eventi sono praticamente i flussi del River selezionato! Perci�:
	 * - invece di creare una classe Event, ha usato la classe Flow e perci� ha implementato
	 * 	 su di essa il Comparable
	 * - siccome r.getFlows() restituisce gi� l'elenco di tutti i vari flows in ordine cronolo
	 *   gico, allora � semplicemente esso la nostra eventList! Solo che, siccome � una List e
	 *   non una coda, non faccio il ciclo while((e=eventList.poll())!=null){ ...} ma faccio 
	 *   semplicemente:
	 *   		for (Flow flow : r.getFlows()) {...}

	 */

	
}
