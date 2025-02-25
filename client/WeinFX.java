package t4WS7Client;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import t4WS7Klassen.Wein;

public class WeinFX {

	private Wein modellWein;
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty desc;
	private SimpleDoubleProperty preis;

	public WeinFX(Wein modellWein) {
		super();
		this.modellWein = modellWein;
		id = new SimpleIntegerProperty(modellWein.getId());
		name = new SimpleStringProperty(modellWein.getName());
		desc = new SimpleStringProperty(modellWein.getDesc());
		preis = new SimpleDoubleProperty(modellWein.getPreis());
	}

	public Wein getServerWein() {
		return null;
	}
	
	public void setServerWein(Wein serverWein) {
		
	}
	
	public Wein getModellWein() {
		return modellWein;
	}

	public final SimpleIntegerProperty idProperty() {
		return this.id;
	}

	public final int getId() {
		return this.idProperty().get();
	}

	public final void setId(final int id) {
		this.idProperty().set(id);
		modellWein.setId(id);
	}

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
		modellWein.setName(name);
	}

	public final SimpleStringProperty descProperty() {
		return this.desc;
	}

	public final String getDesc() {
		return this.descProperty().get();
	}

	public final void setDesc(final String desc) {
		this.descProperty().set(desc);
		modellWein.setDesc(desc);
	}

	public final SimpleDoubleProperty preisProperty() {
		return this.preis;
	}

	public final double getPreis() {
		return this.preisProperty().get();
	}

	public final void setPreis(final double preis) {
		this.preisProperty().set(preis);
		modellWein.setPreis(preis);
	}

	@Override
	public String toString() {
		return "WeinFX [modellWein=" + modellWein + ", id=" + id + ", name=" + name + ", desc=" + desc + ", preis="
				+ preis + "]";
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<wein>");
		sb.append("<weinid>" + id + "</weinid>");
		if(name != null)
			sb.append("<name>" + name + "</name>");
		if(desc != null)
			sb.append("<desc>" + desc + "</desc>");
		sb.append("<preis>" + preis + "</preis>");
		sb.append("</wein>");
		return sb.toString();
	}
}
