package it.polito.tdp.rivers.model;

public class SimulationResult {

	private double Q;
	private double Cavg;	
	private int numGiorniOff;
	private int totGiorni;
	
	public SimulationResult(double q, double c,int n1,int n){
		Q=q;
		Cavg=c;
		numGiorniOff=n1;
		totGiorni=n;
	}

	public double getQ() {
		return Q;
	}



	public void setQ(double q) {
		Q = q;
	}



	public double getCavg() {
		return Cavg;
	}

	public void setCavg(double cavg) {
		Cavg = cavg;
	}

	public int getNumGiorniOff() {
		return numGiorniOff;
	}

	public void setNumGiorniOff(int numGiorniOff) {
		this.numGiorniOff = numGiorniOff;
	}

	public int getTotGiorni() {
		return totGiorni;
	}

	public void setTotGiorni(int totGiorni) {
		this.totGiorni = totGiorni;
	}

	@Override
	public String toString() {
		return "\n Risultati della Simulazione:"
				+ "\n capienza totale del bacino = "+Q
				+ "\n occupazione media = " + Cavg 
				+ "\n numero di giorni al di sotto dell' erogazione minima = " + numGiorniOff 
				+ "\n numero di giorni totali considerati = " + totGiorni + "\n";
	}
	
	
}
