package t4WS7Klassen;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WarenkorbElement {

	private int id;
	private long kundenId;
	private Wein wein;
	
	public WarenkorbElement(int id, long kundenId, Wein wein) {
		super();
		this.id = id;
		this.kundenId = kundenId;
		this.wein = wein;
	}
	public WarenkorbElement() {
		super();
	}
	
	public WarenkorbElement(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader sr = new StringReader(xmlString);
			XMLHandler xmlHandler = new XMLHandler();
			sp.parse(new InputSource(sr), xmlHandler);
			id = xmlHandler.getWke().getId();
			kundenId = xmlHandler.getWke().getKundenId();
			wein = xmlHandler.getWke().getWein();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<warenkorbelement>");
		sb.append("<warenkorbelementid>" + id + "</warenkorbelementid>");
		if(kundenId != 0)
			sb.append("<warenkorbelementkundeid>" + kundenId + "</warenkorbelementkundeid>");
		sb.append("<wein>");
		sb.append("<weinid>" + wein.getId() + "</weinid>");
		if(wein.getName() != null)
			sb.append("<name>" + wein.getName() + "</name>");
		if(wein.getDesc() != null)
			sb.append("<desc>" + wein.getDesc() + "</desc>");
		sb.append("<preis>" + wein.getPreis() + "</preis>");
		sb.append("</wein>");
		sb.append("</warenkorbelement>");
		return sb.toString();
	}
	
	public int getId() {
		return id;
	}
	public long getKundenId() {
		return kundenId;
	}
	public Wein getWein() {
		return wein;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setKundenId(long kundenId) {
		this.kundenId = kundenId;
	}
	public void setWein(Wein wein) {
		this.wein = wein;
	}
	@Override
	public String toString() {
		return "WarenkorbElement [id=" + id + ", kundenId=" + kundenId + ", wein=" + wein + "]";
	}
	
	
}
