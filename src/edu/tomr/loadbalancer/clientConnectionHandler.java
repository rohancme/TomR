package edu.tomr.loadbalancer;

import java.net.Socket;

public class clientConnectionHandler implements Runnable {
	private Socket clientSocket;

	public clientConnectionHandler(Socket clientConnection) {
		clientSocket = clientConnection;
	}

	

	@Override
	public void run() {
		
		
	}

}
