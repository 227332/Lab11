package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import it.polito.tdp.rivers.model.SimulationResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RiversController {

	Model model = null;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<River> boxRiver;

	@FXML
	private TextField txtEndDate;

	@FXML
	private TextField txtNumMeasurements;

	@FXML
	private TextArea txtResult;

	@FXML
	private TextField txtStartDate;

	@FXML
	private TextField txtFMed;

	@FXML
	private Button btnSimula;

	@FXML
	private TextField txtK;

	@FXML
	void initialize() {
		assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Rivers.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Rivers.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;

		try {
			boxRiver.getItems().addAll(model.getRivers());
			boxRiver.valueProperty().addListener(new ChangeListener<River>() {
				@Override
				public void changed(ObservableValue<? extends River> observable, River oldValue, River newValue) {
					txtStartDate.setText(model.getStartDate(newValue).toString());
					txtEndDate.setText(model.getEndDate(newValue).toString());
					txtNumMeasurements.setText(String.valueOf(model.getNumMeasurements(newValue)));
					txtFMed.setText(String.valueOf(model.getFMed(newValue)));
				}
			});

			// Just another way to set an event handler
			// http://code.makery.ch/blog/javafx-8-event-handling-examples/
			btnSimula.setOnAction((event) -> {
				// Button was clicked, get K value
				try {
					double k = Double.parseDouble(txtK.getText());
					SimulationResult sr = model.simulate(boxRiver.getValue(), k);
					txtResult.setText("Number of days in which it cannot guarantee the minimum capacity: "
							+ sr.getNumberOfDays() + "\n");
					txtResult.appendText("Average Capacity: " + sr.getAvgC() + "\n");
					txtResult.appendText("SIMULATION FINISHED!\n");
				} catch (NumberFormatException nfe) {
					txtResult.setText("Some Error Occured: " + nfe.getMessage());
				}
			});

		} catch (RuntimeException re) {
			txtResult.setText("Some Error Occured: " + re.getMessage());
		}
	}

}
