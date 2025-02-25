package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WarenKorb {

	private ArrayList<WarenkorbElement> elemente;

	public WarenKorb(ArrayList<WarenkorbElement> elemente) {
		super();
		this.elemente = elemente;
	}
	
	public WarenKorb(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader sr = new StringReader(xmlString);
			XMLHandler xmlHandler = new XMLHandler();
			sp.parse(new InputSource(sr), xmlHandler);
			elemente = xmlHandler.getWk();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<WarenkorbElement> getElemente() {
		return elemente;
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<warenkorb>");
		if (elemente != null)
			for (WarenkorbElement ein : elemente)
				sb.append(ein.toXML());
		sb.append("</warenkorb>");
		return sb.toString();
	}
}
