package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event> {
	
	private LocalDate day;
	private Flow flow;
	//c'è solo un tipo evento, ossia il flusso iniziale di quel giorno, perciò non devo
	//definire un attributo TypeEvent
	
	public Event(LocalDate d, Flow f){
		day=d;
		flow=f;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flow == null) ? 0 : flow.hashCode());
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
		Event other = (Event) obj;
		if (flow == null) {
			if (other.flow != null)
				return false;
		} else if (!flow.equals(other.flow))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [flow=" + flow.toString() + "]";
	}

	@Override
	public int compareTo(Event e) {
		// TODO Auto-generated method stub
		return day.compareTo(e.getDay());
	}
	
	
	
	

}
