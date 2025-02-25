package t4WS7Klassen;

public class Wein {
	private int id;
	private String name;
	private String desc;
	private double preis;

	public Wein(int id, String name, String desc, double preis) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.preis = preis;
	}

	public Wein() {
		super();
	}

	@Override
	public String toString() {
		return "Wein [id=" + id + ", name=" + name + ", desc=" + desc + ", preis=" + preis + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
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
