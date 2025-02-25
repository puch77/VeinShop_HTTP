package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Kunde {
	
	private long userId;
	private String name;
	private LocalDate datum;

	public Kunde() {
		super();
	}

	public Kunde(long userId, String name, LocalDate datum) {
		super();
		this.userId = userId;
		this.name = name;
		this.datum = datum;
	}

	public Kunde(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader sr = new StringReader(xmlString);
			XMLHandler xmlHandler = new XMLHandler();
			sp.parse(new InputSource(sr), xmlHandler);
			userId = xmlHandler.getKunde().getUserId();
			name = xmlHandler.getKunde().getName();
			datum = xmlHandler.getKunde().getDatum();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<kunde>");
		sb.append("<kundeid>" + userId + "</kundeid>");
		if (name != null)
			sb.append("<kundename>" + name + "</kundename>");
		if (datum != null)
			sb.append("<datum>" + datum + "</datum>");
		sb.append("</kunde>");
		return sb.toString();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

}
