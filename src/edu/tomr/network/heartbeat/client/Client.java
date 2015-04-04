package edu.tomr.network.heartbeat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.network.base.ConnectionAddress;
import edu.tomr.protocol.HeartBeatMessage;

public class Client {

	private ConnectionAddress serverAddress;
	private Socket connection;
	
	public Client(ConnectionAddress serverAddress) {
		
		this.serverAddress = serverAddress;
	}
	
	public Client(String ipAddress, int port) {
		
		this.serverAddress = new ConnectionAddress(ipAddress, port);
	}

	public void initiateConnection() throws UnknownHostException, IOException {
	
		connection = new Socket(serverAddress.getIpAddress(), serverAddress.getPortNumber());
	}
	
	public void sendHeartBeat(ConnectionAddress ownerAddress) throws IOException{
		HeartBeatMessage beat = new HeartBeatMessage(ownerAddress);
		
		new ObjectMapper().writeValue(connection.getOutputStream(), beat);
	}
	
	public void closeConnection() throws IOException {
		
		connection.close();
	}
}
