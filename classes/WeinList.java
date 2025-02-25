package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WeinList {
	private ArrayList<Wein> weine;

	public WeinList(ArrayList<Wein> weine) {
		super();
		this.weine = weine;
	}

	public WeinList() {
		super();
	}

	public WeinList(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader sr = new StringReader(xmlString);
			XMLHandler xmlHandler = new XMLHandler();
			sp.parse(new InputSource(sr), xmlHandler);
			weine = xmlHandler.getWeine();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "WeinList [weine=" + weine + "]";
	}

	public ArrayList<Wein> getWeine() {
		return weine;
	}

	public void setWeine(ArrayList<Wein> weine) {
		this.weine = weine;
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<weinliste>");
		if (weine != null)
			for (Wein einWein : weine)
				sb.append(einWein.toXML());
		sb.append("</weinliste>");
		return sb.toString();
	}
}
