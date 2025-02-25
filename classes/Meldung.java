package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Meldung {

	private String text;

	public Meldung(String text) {
		super();
		if(text == null || text.length() == 0)
			return;
		if(!text.startsWith("<"))
			this.text = text;
		else {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
				SAXParser sp = spf.newSAXParser();
				StringReader sr = new StringReader(text);
				XMLHandler xmlHandler = new XMLHandler();
				sp.parse(new InputSource(sr), xmlHandler);
				text = xmlHandler.getMeldung().getText();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Meldung() {
		super();
	}
	
	public String toXML() {
		return "<meldung><text>" + text + "</text></meldung>";
	}

	@Override
	public String toString() {
		return "Meldung [text=" + text + "]";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
