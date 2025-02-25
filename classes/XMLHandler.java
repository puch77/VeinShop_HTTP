package t4WS7Klassen;

import java.time.LocalDate;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

	public static final String XML_PROLOG_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";
	
	private String text;
	private ArrayList<Wein> weine;
	private Wein wein;
	private Meldung meldung;
	private ArrayList<Kunde> kunden;
	private Kunde kunde;
	private WarenkorbElement wke;
	private ArrayList<WarenkorbElement> wk;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch(qName.toUpperCase()) {
		case "WEINLISTE":
			weine = new ArrayList<Wein>();
			break;
		case "WEIN":
			wein = new Wein();
			break;
		case "MELDUNG":
			meldung = new Meldung();
			break;
		case "KUNDELISTE":
			kunden = new ArrayList<Kunde>();
			break;
		case "KUNDE":
			kunde = new Kunde();
			break;
		case "WARENKORB":
			wk = new ArrayList<WarenkorbElement>();
			break;
		case "WARENKORBELEMENT":
			wke = new WarenkorbElement();
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch(qName.toUpperCase()) {
		case "WEINID":
			wein.setId(Integer.parseInt(text));
			break;
		case "NAME":
			wein.setName(text);
			break;
		case "DESC":
			wein.setDesc(text);
			break;
		case "PREIS":
			wein.setPreis(Double.parseDouble(text));
			break;
		case "WEIN":
			if(weine != null)
				weine.add(wein);
			else
				wke.setWein(wein);
			break;
		case "TEXT":
			meldung.setText(text);
			break;
		case "KUNDEID":
			kunde.setUserId(Long.parseLong(text));
			break;
		case "KUNDENAME":
			kunde.setName(text);
			break;
		case "DATUM":
			kunde.setDatum(LocalDate.parse(text));
			break;
		case "KUNDE":
			if(kunden != null)
				kunden.add(kunde);
			break;
		case "WARENKORBELEMENTID":
			wke.setId(Integer.parseInt(text));
			break;
		case "WARENKORBELEMENTKUNDEID":
			wke.setKundenId(Long.parseLong(text));
			break;
		case "WARENKORBELEMENT":
			if(wk != null) {
				wk.add(wke);
			}
			break;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		text = new String(ch, start, length);
	}

	public ArrayList<Wein> getWeine() {
		return weine;
	}

	public Meldung getMeldung() {
		return meldung;
	}

	public ArrayList<Kunde> getKunden() {
		return kunden;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public WarenkorbElement getWke() {
		return wke;
	}

	public ArrayList<WarenkorbElement> getWk() {
		return wk;
	}	
}
