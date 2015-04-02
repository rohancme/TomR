package edu.tomr.network.heartbeat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
		
		beat.toJSON(connection.getOutputStream(), beat);
		connection.getOutputStream().flush();
		
	}
	
	public void destory() throws IOException {
		
		connection.close();
	}
}
