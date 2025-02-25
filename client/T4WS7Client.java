package t4WS7Client;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import t4WS7Klassen.Kunde;
import t4WS7Klassen.WarenkorbElement;
import t4WS7Klassen.Wein;
import t4WS7Klassen.WeinList;

public class T4WS7Client extends Application {

	Kunde k;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		ObservableList<WeinFX> olWeine = FXCollections.observableArrayList();

		try {
			String line = ServiceFunctions.get("weinliste", null);
			WeinList wl = new WeinList(line);
			for (Wein einWein : wl.getWeine())
				olWeine.add(new WeinFX(einWein));
		} catch (VinothekException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}

		TableColumn<WeinFX, Integer> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		idCol.setPrefWidth(75);
		TableColumn<WeinFX, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(150);
		TableColumn<WeinFX, Double> preisCol = new TableColumn<>("Preis");
		preisCol.setCellValueFactory(new PropertyValueFactory<>("preis"));
		preisCol.setPrefWidth(75);

		TableColumn<WeinFX, String> descCol = new TableColumn<>("Beschreibung");
		descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
		descCol.setPrefWidth(400);

		TableView<WeinFX> tvWein = new TableView<>(olWeine);
		tvWein.getColumns().addAll(idCol, nameCol, preisCol, descCol);

		TextField txtName = new TextField();
		txtName.setPrefWidth(100);
		Button login = new Button("Login");
		login.setDisable(true);
		Button korb = new Button("in den Korb");
		korb.setDisable(true);
		Button kassa = new Button("zur Kassa gehen");
		kassa.setDisable(true);
		Button alle = new Button("Alle Bestellungen");
		alle.setDisable(false);
		Button close = new Button("Beenden");

		HBox hb = new HBox(10, new Label("Name"), txtName, login, korb, kassa, alle, close);
		hb.setPadding(new Insets(5));

		VBox vb = new VBox(10, tvWein, hb);
		vb.setPadding(new Insets(5));
		arg0.setScene(new Scene(vb));
		arg0.setTitle("T4WS7Client");
		arg0.show();

		close.setOnAction(e -> arg0.close());

		txtName.setOnKeyReleased(e -> login.setDisable(txtName.getText().length() == 0));

		login.setOnAction(e -> {
			k = new Kunde(System.currentTimeMillis(), txtName.getText(), LocalDate.now());
			String xmlData = k.toXML();

			try {
				ServiceFunctions.post("kunde", String.valueOf(System.currentTimeMillis()), xmlData);
				// Optionally, you can show a success message or perform additional actions.
				new Alert(AlertType.INFORMATION, "Login successful " + txtName.getText().toUpperCase()).showAndWait();

				// Enable buttons after successful login
				korb.setDisable(false);
			} catch (VinothekException ex) {
				ex.printStackTrace();
				new Alert(AlertType.ERROR, ex.toString()).showAndWait();
			}
		});

		korb.setOnAction(e -> {
			WeinFX selectedWein = tvWein.getSelectionModel().getSelectedItem();

			if (selectedWein != null && k != null) {
				try {
					Wein wein = selectedWein.getModellWein(); 
					//System.out.println(wein.toString());
		            long kundenId = k.getUserId();

		            WarenkorbElement warenkorbElement = new WarenkorbElement(0, kundenId, wein);
		            String xmlData = warenkorbElement.toXML();
		            
		            //System.out.println("\nWarenkorbelement: " + warenkorbElement.toString());
		            
		            ServiceFunctions.post("warenkorbelement", String.valueOf(kundenId), xmlData);

		            //new Alert(AlertType.INFORMATION, "Produkt erfolgreich zum Warenkorb hinzugefügt").showAndWait();

		            kassa.setDisable(false);
				} catch (VinothekException ex) {
					ex.printStackTrace();
					new Alert(AlertType.ERROR, ex.toString()).showAndWait();
				}
			} 
		});
		kassa.setOnAction(e -> {
			new warenkorbDialog(k.getUserId()).showAndWait();
		});

		alle.setOnAction(e -> {
			new kundenbestellungenDialog().showAndWait();
		});
	}

}
