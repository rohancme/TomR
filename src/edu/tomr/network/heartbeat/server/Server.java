package edu.tomr.network.heartbeat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.heartbeat.exception.NodeGoneDownException;
import edu.tomr.protocol.HeartBeatMessage;
import edu.tomr.utils.Constants;

public class Server {

	private ServerBeatController controller;
	private int portNumber;
	private ServerSocket server;

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public Server(ServerBeatController controller, int portNumber) {
		this.controller = controller;
		this.portNumber = portNumber;
	}

	public void startServer() throws IOException{
		Constants.globalLog.debug("started listening for connections");
		server = new ServerSocket(this.portNumber);
		try {
			while (true) {
				new Handler(server.accept()).start();
			}
		} finally {
			server.close();
		}
	}

	private class Handler extends Thread {
		private Socket client;

		public Handler(Socket client){
			this.client = client;
		}

		public void run() {
			HeartBeatMessage message = null;
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e1) {
				Constants.globalLog.debug("Server:run buff reader");
				e1.printStackTrace();
			}

			ObjectMapper mapper = new ObjectMapper();
			try {

				message = mapper.readValue(in, HeartBeatMessage.class);
			} catch (JsonParseException | JsonMappingException e) {

				e.printStackTrace();
			} catch (IOException e) {
				Constants.globalLog.debug("object deserialed error");
				e.printStackTrace();
			}
			if (message == null) {
				return;
			}
			synchronized (controller.clients) {

				Constants.globalLog.debug("updated ip address and timestamp for server at: "
						+message.toString()+" with time: "+message.getTimeStamp());
				if(controller.clients.containsKey(message.toString())) {
					new TimerCheck(message.toString()).start();
				}
				controller.clients.put(message.toString(), message.getTimeStamp().getTime());
			}

			try {
				client.close();
			} catch (IOException e) {
				Constants.globalLog.debug("client connection closed");
				e.printStackTrace();
			}
		}
	}

	private class TimerCheck extends Thread {

		private String clientKey;

		public TimerCheck(String clientKey) {
			this.clientKey = clientKey;
		}

		public void run() {
			boolean running = true;

			while(running) {
				try {
					Thread.sleep(5000);
					checkTiming(controller.clients);
				} catch (InterruptedException e) {

					e.printStackTrace();
				} catch (NodeGoneDownException e) {

					Constants.globalLog.debug("NodeGoneDownException thrown");
					running = false;
					e.printStackTrace();
				}
			}

			Constants.globalLog.debug("Checker thread stopping");
			controller.clients.remove(clientKey);
		}

		public void checkTiming(Map<String, Long> clients) throws NodeGoneDownException {

			Long d = clients.get(clientKey);
			if((Calendar.getInstance().getTimeInMillis() - d) > 6000 ) {
				throw new NodeGoneDownException("Node at address "+clientKey+" is down");
			}
		}
	}

}
