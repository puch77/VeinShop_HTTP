package t4WS7Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class T4WS7Server {

	public static void main(String[] args) {
		InetAddress inet;
		try {
			inet = InetAddress.getByName("localhost");
			InetSocketAddress addr = new InetSocketAddress(inet, 4711);
			HttpServer server = HttpServer.create(addr, 0);
			server.createContext("/", new T4WS7Handler());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			System.out.println("Vinothekserver gestartet - jetzt Zeit für Client. ");
		
			Datenbank.createKundeTable();
			Datenbank.createKorbTable();
			
			System.in.read();
			server.stop(0);
			((ExecutorService) server.getExecutor()).shutdown();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

}
