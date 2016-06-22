package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Flow {

	private int id;
	private LocalDate day;
	private double flow;
	private River river;

	public Flow(LocalDate day, double flow, River river) {
		this.day = day;
		this.flow = flow;
		this.river = river;
	}
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public River getRiver() {
		return river;
	}



	public void setRiver(River river) {
		this.river = river;
	}



	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flow other = (Flow) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Flow [id=" + id + ", day=" + day + ", flow=" + flow + ", river=" + river + "]";
	}

	

}
