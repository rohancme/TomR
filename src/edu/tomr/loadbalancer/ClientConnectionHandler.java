package edu.tomr.loadbalancer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import network.Connection;
import network.requests.NWRequest;

import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.protocol.ClientServiceRequestPayload;
import edu.tomr.utils.ConfigParams;

public class ClientConnectionHandler implements Runnable {
	private Socket clientSocket;
	private UUID clientUID;
	private static int turnOf = 0;
	
	static{
		ConfigParams.loadProperties();
	}

	public ClientConnectionHandler(Socket clientConnection) {
		this.clientSocket = clientConnection;
		this.clientUID = generateUID();
	}

	

	private static UUID generateUID() {
		return UUID.randomUUID();
		
	}



	@Override
	public void run() {
		String IPAddress = null;
		try{
			IPAddress = getIPAddress();
		}
		catch(NullPointerException e){
			System.out.println("NULL value returned for the IPAddress which is servicing the Client");
			e.printStackTrace();
		}
		
		ClientServiceRequestPayload servicePayload = new ClientServiceRequestPayload(IPAddress);
		ClientServiceMessage serviceMessage = new ClientServiceMessage(servicePayload);
		NWRequest serviceRequest = new NWRequest(clientUID.toString(), serviceMessage);
		//Send this request to the client. 
		Connection clientServiceConnection = new Connection(clientSocket);
		clientServiceConnection.send_request(serviceRequest);
		//close client socket
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Erro while trying to close the socket");
			e.printStackTrace();
		}
		//Exit Thread
		
	}



	private synchronized String getIPAddress() {
		String IPAddress = null;
		try{
		if(turnOf == ConfigParams.getIpAddresses().size() - 1){
			turnOf = 0;
			IPAddress = ConfigParams.getIpAddresses().get(turnOf);
			}
		else{
			IPAddress = ConfigParams.getIpAddresses().get(turnOf);
			turnOf++;
			}
		}
		catch(Exception e){
			System.out.println("Error while trying to access the IP Addresses for scheduling");
			e.printStackTrace();
			
		}
		return IPAddress;
		
	}

}