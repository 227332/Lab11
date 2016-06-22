package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
/*
 * RICORDA: ChangeListener è quello in javafx.beans.value e non in javax.swing.event
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RiversController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<River> boxRiver;

    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtNumMeasurements;

    @FXML
    private TextField txtFMed;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnSimula;

    @FXML
    private TextArea txtResult;
    
    private Model model;
    
	public void setModel(Model model) {
		// TODO Auto-generated method stub
		this.model=model;
		
		//popolo la ComboBox
		boxRiver.getItems().addAll(model.getAllRivers());
		
		/*
		 * OSS:
		 * Nel caso della ComboBox conviene usare sempre un Listener per far sì che 
		 * quando cambio la selezione nel menu a endina automaticamente cambino i valori
		 * delle varie finestre di testo ... è meglio lasciare la creazioni di metodi come
		 * doSimula() e altri event handler al caso di tipi Button e non ComboBox :)
		 */
		boxRiver.valueProperty().addListener(new ChangeListener<River>(){

			@Override
			public void changed(ObservableValue<? extends River> r, River oldValue, River newValue) {
				// TODO Auto-generated method stub
				
				/*
				 * OSS: ricorda che se hai null non puoi fare null.toString o altre cose del genere perché ti lancia un'eccezione!
				 * In qst DB a dire il vero non capita mai, però volendo si può mettere questo controlloin più
				 */
				if(model.getStartDate(newValue)!=null){
					txtStartDate.setText(model.getStartDate(newValue).toString());
					txtEndDate.setText(model.getEndDate(newValue).toString());
				}
				else{
					txtStartDate.setText("");
					txtEndDate.setText("");
				}
				//RICORDA: in txtNumMeasurements devi metterci un tipo String nel Text
				txtNumMeasurements.setText(String.valueOf(model.getNumMeasurements(newValue)));
				txtFMed.setText(String.valueOf(model.getFMed(newValue)));

			}
			
		});
		
	}
    

    @FXML
    void doSimula(ActionEvent event) {
    	//RICORDA: in una ComboBox si fa così il check per vedere se è stato
    	//selezionato un suo elemento
    	River r=boxRiver.getSelectionModel().getSelectedItem();
    	if(r==null){
    		txtResult.setText("Errore: seleziona un Fiume per la simulazione!");
    		return;
    	}
    	//ATT: per i tipi text si fa il check con isEmpty() e NON con null!!!
    	if(txtK.getText().isEmpty()){
    		txtResult.setText("Errore: inserire un fattore K per la simulazione!");
    		return;
    	}
    	
    	double K = Double.parseDouble(txtK.getText());
    	if(K<=0){
    		txtResult.setText("Errore: il fattore K deve essere strettamente positivo!");
    		return;
    	}
    	
    	txtResult.setText(model.simula(r,K).toString());
    	

    }

    @FXML
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Rivers.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Rivers.fxml'.";

    }


}

