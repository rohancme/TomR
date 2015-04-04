package edu.tomr.network.heartbeat.server;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Controller {

	Map<String, Date> clients;
	Server beatServer;
	
	public void start() {
		
		int portNumber = 8080;
		
		clients = new HashMap<String, Date>();
		beatServer = new Server(this, portNumber);
		try {
			beatServer.startServer();
		} catch (IOException e) {
			System.out.println("server controller start: IOException");
			e.printStackTrace();
		}
		
		//spawn a thread to check messages from client
	}
	
	public static void main(String []args) {
		Controller c = new Controller();
		c.start();
	}
}
