package t4WS7Client;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import t4WS7Klassen.WarenKorb;
import t4WS7Klassen.WarenkorbElement;

public class warenkorbDialog extends Dialog<ButtonType> {

	public warenkorbDialog(Long userId) {
		ObservableList<WarenkorbElement> olKorb = FXCollections.observableArrayList();

		try {
			String line = ServiceFunctions.get("warenkorb", String.valueOf(userId));
			WarenKorb wk = new WarenKorb(line);
			for (WarenkorbElement ein : wk.getElemente())
				olKorb.add(ein);
		} catch (VinothekException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}

		TableColumn<WarenkorbElement, Integer> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		idCol.setPrefWidth(75);

		TableColumn<WarenkorbElement, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getWein().getName()));
		nameCol.setPrefWidth(150);
		TableColumn<WarenkorbElement, Double> preisCol = new TableColumn<>("Preis");
		preisCol.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getWein().getPreis()).asObject());
		preisCol.setPrefWidth(75);

		TableView<WarenkorbElement> tvKorb = new TableView<>(olKorb);
		tvKorb.getColumns().addAll(idCol, nameCol, preisCol);

		Button delBtn = new Button("Entfernen");

		delBtn.setOnAction(e-> {
			try {
				ServiceFunctions.delete("warenkorbelement", String.valueOf(tvKorb.getSelectionModel().getSelectedItem().getId()));
				olKorb.remove(tvKorb.getSelectionModel().getSelectedItem());
				tvKorb.refresh();
			} catch (VinothekException e1) {
				e1.printStackTrace();
			}
		});
		
		VBox vb = new VBox(10, tvKorb, delBtn);
		this.getDialogPane().setContent(vb);
		this.setTitle("Warenkorb");

		this.getDialogPane().getButtonTypes().add(new ButtonType("Bezahlen", ButtonData.CANCEL_CLOSE));
	}
}
