package t4WS7Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import t4WS7Klassen.Kunde;
import t4WS7Klassen.KundeList;
import t4WS7Klassen.Meldung;
import t4WS7Klassen.WarenKorb;
import t4WS7Klassen.WarenkorbElement;
import t4WS7Klassen.WeinList;
import t4WS7Klassen.XMLHandler;

public class T4WS7Handler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		System.out.println(uri + " " + requestMethod);
		String path = uri.getPath();
		if (path.startsWith("/")) // path looks like /weinliste -> ohne /
			path = path.substring(1);
		String[] paths = path.split("/");
		if (requestMethod.equalsIgnoreCase("GET"))
			get(exchange, paths);
		else if (requestMethod.equalsIgnoreCase("POST"))
			post(exchange, paths);
		else if (requestMethod.equalsIgnoreCase("DELETE"))
			delete(exchange, paths);
		else {
			setResponse(exchange, 400, new Meldung("Falsche HTTP Befehl " + requestMethod).toXML());
		}
	}

	private void delete(HttpExchange exchange, String[] paths) {
		int statusCode = 204;
		String xmlString = "";
		if (paths.length == 2 && paths[0].equals("warenkorbelement")) {
			try {
				Datenbank.deleteKorbElement(Integer.parseInt(paths[1]));
			} catch (SQLException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			}
		} else {
			statusCode = 400;
			xmlString = new Meldung("Falsche URL").toXML();
		}
		setResponse(exchange, statusCode, xmlString);
	}

	private void post(HttpExchange exchange, String[] paths) { 
		int statusCode = 201;
		String xmlString = "";
		InputStream is = exchange.getRequestBody();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		if (paths.length == 2 && paths[0].equals("kunde")) {
			try {
				String line = br.readLine();
				System.out.println("\trequestbody = '" + line + "'");
				Datenbank.insertKunde(new Kunde(line));
			} catch (IOException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (paths.length == 2 && paths[0].equals("warenkorbelement")) {
			try {
				String line = br.readLine();
				System.out.println("\trequestbody = '" + line + "'");
				Datenbank.insertKorbElement(new WarenkorbElement(line));
				System.out.println("Element added");
			} catch (IOException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			statusCode = 400;
			xmlString = new Meldung("Falsche URL").toXML();
		}
		setResponse(exchange, statusCode, xmlString);
	}

	private void get(HttpExchange exchange, String[] paths) {
		int statusCode = 200;
		String xmlString = "";
		if (paths.length == 1 && paths[0].equals("weinliste")) {
			try {
				WeinList wl = Datenbank.leseWeine();
				xmlString = wl.toXML();
			} catch (SQLException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 1 && paths[0].equals("kundeliste")) {
			try {
				KundeList kl = Datenbank.leseKunden();
				xmlString = kl.toXML();
			} catch (SQLException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("warenkorb")) {
			try {
				WarenKorb wk = Datenbank.leseWarenkorb(Long.valueOf(paths[1]));
				xmlString = wk.toXML();
			} catch (SQLException e) {
				statusCode = 500;
				xmlString = new Meldung(e.toString()).toXML();
			}
		} else {
			statusCode = 400;
			xmlString = new Meldung("Falsche URL").toXML();
		}
		setResponse(exchange, statusCode, xmlString);
	}

	private void setResponse(HttpExchange exchange, int statusCode, String xmlString) {
		if (xmlString.length() > 0)
			xmlString = XMLHandler.XML_PROLOG_UTF8 + xmlString;
		System.out.println("\tstatusCode = " + statusCode + "\n\tresponsebody = " + xmlString + " ");
		exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=UTF-8");
		byte[] bytes = xmlString.getBytes(StandardCharsets.UTF_8);
		try {
			exchange.sendResponseHeaders(statusCode, statusCode != 204 ? bytes.length : -1);
			OutputStream os = exchange.getResponseBody();
			if (statusCode != 204)
				os.write(bytes);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
