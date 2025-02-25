package t4WS7Client;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import t4WS7Klassen.Kunde;
import t4WS7Klassen.KundeList;
import t4WS7Klassen.WarenKorb;
import t4WS7Klassen.WarenkorbElement;

public class kundenbestellungenDialog extends Dialog<ButtonType>{

	public kundenbestellungenDialog() {
		
		ObservableList<Kunde> olKunde = FXCollections.observableArrayList();
		ObservableList<WarenkorbElement> olKorb = FXCollections.observableArrayList();
		
		try {
			String line = ServiceFunctions.get("kundeliste", null);
			KundeList kl = new KundeList(line);
			for (Kunde ein : kl.getUsers())
				olKunde.add(ein);
		} catch (VinothekException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}

		TableColumn<Kunde, Long> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
		idCol.setPrefWidth(100);

		TableColumn<Kunde, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(150);
		TableColumn<Kunde, LocalDate> datumCol = new TableColumn<>("Datum");
		datumCol.setCellValueFactory(new PropertyValueFactory<>("datum"));
		datumCol.setPrefWidth(100);

		TableView<Kunde> tvKunde = new TableView<>(olKunde);
		tvKunde.getColumns().addAll(idCol, nameCol, datumCol);
		tvKunde.setPrefSize(350, 250);
		
		TableColumn<WarenkorbElement, Integer> idColWk = new TableColumn<>("Id");
		idColWk.setCellValueFactory(new PropertyValueFactory<>("id"));
		idColWk.setPrefWidth(75);

		TableColumn<WarenkorbElement, String> nameColWk = new TableColumn<>("Name");
		nameColWk.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getWein().getName()));
		nameColWk.setPrefWidth(150);
		TableColumn<WarenkorbElement, Double> preisColWk = new TableColumn<>("Preis");
		preisColWk.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getWein().getPreis()).asObject());
		preisColWk.setPrefWidth(75);
		
		TableView<WarenkorbElement> tvBest = new TableView<>(olKorb);
		tvBest.getColumns().addAll(idColWk, nameColWk, preisColWk);
		tvBest.setPrefSize(350, 250);
		
		tvKunde.setOnMouseClicked(e -> {
			Kunde selectedElement = tvKunde.getSelectionModel().getSelectedItem();
            if (selectedElement != null) {
            	olKorb.clear();
            	try {
        			String line = ServiceFunctions.get("warenkorb", String.valueOf(selectedElement.getUserId()));
        			WarenKorb wk = new WarenKorb(line);
        			for (WarenkorbElement ein : wk.getElemente())
        				olKorb.add(ein);
        		} catch (VinothekException e1) {
        			new Alert(AlertType.ERROR, e1.toString()).showAndWait();
        		}
            }
		});
		
		VBox vb = new VBox(10, new Label("Alle Kunden"), tvKunde, new Label("Alle Bestellungen des ausgewählten Kunde"), tvBest);
		this.getDialogPane().setContent(vb);
		this.setTitle("Alle Kundenbestellungen");

		this.getDialogPane().getButtonTypes().add(new ButtonType("Beenden", ButtonData.CANCEL_CLOSE));
	
	}
}
