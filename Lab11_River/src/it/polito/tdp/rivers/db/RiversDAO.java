package it.polito.tdp.rivers.db;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RiversDAO {

	public List<River> getAllRivers() {
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}

			conn.close();

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException();
		}

		return rivers;
	}

	/*
	 * OSS: devo passare in input la lista con tutti gli oggetti River perchè
	 * così posso in ogni oggetto Flow indicare il puntatore al River corrispondente
	 */
	public List<Flow> getAllFlows(List<River> rivers) {
		final String sql = "SELECT id, day, flow, river FROM flow";

		List<Flow> flows = new LinkedList<Flow>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				flows.add(new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow"),
						rivers.get(rivers.indexOf(new River(res.getInt("river"))))));
				/*
				 * ATTENZIONE: nota come all'oggetto Flow non creo un nuovo oggetto River
				 * ma gli passo il puntatore all'oggetto River già creato in precedenza e passato
				 * come parametro di input del metodo!!!
				 */
			}

			/*
			 * RICORDA come funziona la lambda expression nel comparator... In particolare non
			 * devi scrivere return!
			 */
			Collections.sort(flows,(f1,f2)-> f1.getDay().compareTo(f2.getDay()));
			conn.close();

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException();
		}

		return flows;//se non ce ne sono allora ho flows empty e non null
	}

	public static void main(String[] args) {
		RiversDAO dao = new RiversDAO();

		List<River> rivers = dao.getAllRivers();
		System.out.println(rivers);

		List<Flow> flows = dao.getAllFlows(rivers);
		System.out.format("Loaded %d flows\n", flows.size());
		// System.out.println(flows) ;
	}
	
	
	/*
	 * OSS: mi devo creare questo metodo perchè non mi serve il gettAllFlows() datomi sopra
	 */
	public List<Flow> getFlowsByRiver(River river) {
		
		final String sql = "SELECT id, day, flow FROM flow WHERE river=?";

		/*
		 * OSS: in questi casi in cui devi solo aggiungere elementi ad una List ricorda che conviene
		 * la LinkedList!
		 */
		List<Flow> flows = new LinkedList<Flow>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				flows.add(new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow"), river));
			}			
			conn.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return flows;
	}


}
