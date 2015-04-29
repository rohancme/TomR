package edu.tomr.network.heartbeat.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.tomr.utils.Constants;

public class ServerBeatController {

	static int beatPortNumber = 8080;
	
	Map<String, Long> clients;
	Server beatServer;
	
	public void start() {
		
		clients = new HashMap<String, Long>();
		beatServer = new Server(this, beatPortNumber);
		try {
			beatServer.startServer();
		} catch (IOException e) {
			Constants.globalLog.debug("server controller start: IOException");
			e.printStackTrace();
		}
		
		//spawn a thread to check messages from client
	}
	
	public static void main(String []args) {
		ServerBeatController c = new ServerBeatController();
		c.start();
	}
}
