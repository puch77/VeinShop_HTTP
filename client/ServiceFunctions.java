package t4WS7Client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import t4WS7Klassen.Meldung;
import t4WS7Klassen.XMLHandler;

public class ServiceFunctions {

	private static final String BASIS_URL = "http://localhost:4711/";
	
	public static String get(String item, String id) throws VinothekException {
		String url = BASIS_URL + item;
		if(id != null && id.length() > 0)
			url += "/" + id;
		try {
			URI uri = new URI(url);
			HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray()); 
			int statusCode = response.statusCode();
			String line = new String(response.body());
			if(statusCode == 200)
				return line;
			else
				throw new VinothekException(new Meldung(line).getText());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new VinothekException(e.toString());
		}
		
	}
	
	public static void post(String item, String id, String detail) throws 
	VinothekException {
		String url = BASIS_URL + item + "/" + id;
		detail = XMLHandler.XML_PROLOG_UTF8 + detail;
		try {
			URI uri = new URI(url);
			HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "application/xml; charset=UTF-8")
					.POST(BodyPublishers.ofString(detail, StandardCharsets.UTF_8)).build();
				        
	        HttpClient client = HttpClient.newHttpClient();
			
			System.out.println("\nHTTP Request URI: " + uri);
	        System.out.println("HTTP Request Method: " + request.method());
	        System.out.println("HTTP Request Headers: " + request.headers());
	        System.out.println("HTTP Request Body: " + detail);
	        
	        HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray()); 
			
			System.out.println("HTTP Response Code: " + response.statusCode());
	        System.out.println("HTTP Response Headers: " + response.headers());
	        System.out.println("HTTP Response Body: " + new String(response.body()));
	        
			int statusCode = response.statusCode();
	        
			if(statusCode != 201)
				throw new VinothekException(new Meldung(new String(response.body())).getText());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new VinothekException(e.toString());
		}
		
	}
	
	public static void delete(String item, String id) throws VinothekException {
		String url = BASIS_URL + item;
		if(id != null && id.length() > 0)
			url += "/" + id;
		try {
			URI uri = new URI(url);
			HttpRequest request = HttpRequest.newBuilder(uri).DELETE().build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray()); 
			int statusCode = response.statusCode();
			if(statusCode != 204)
				throw new VinothekException(new Meldung(new String(response.body())).getText());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new VinothekException(e.toString());
		}
	}
}
