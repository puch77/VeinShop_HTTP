package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class KundeList {
	private ArrayList<Kunde> users;

	public KundeList(ArrayList<Kunde> users) {
		super();
		this.users = users;
	}
	
	public KundeList(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader sr = new StringReader(xmlString);
			XMLHandler xmlHandler = new XMLHandler();
			sp.parse(new InputSource(sr), xmlHandler);
			users = xmlHandler.getKunden();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<kundeliste>");
		if (users != null)
			for (Kunde eineKunde : users)
				sb.append(eineKunde.toXML());
		sb.append("</kundeliste>");
		return sb.toString();
	}

	public ArrayList<Kunde> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Kunde> users) {
		this.users = users;
	}
	
	
}
