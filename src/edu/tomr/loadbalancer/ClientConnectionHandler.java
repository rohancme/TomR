package edu.tomr.loadbalancer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import network.Connection;
import network.responses.NWResponse;
import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.utils.ConfigParams;
import edu.tomr.utils.Constants;

public class ClientConnectionHandler implements Runnable {
	private Socket clientSocket;
	private UUID clientUID;
	private static int turnOf = 0;
	
	static{
		//ConfigParams.loadProperties();
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
			Constants.globalLog.debug("NULL value returned for the IPAddress which is servicing the Client");
			e.printStackTrace();
		}
		
		ClientServiceMessage serviceMessage = new ClientServiceMessage(IPAddress,UUID.randomUUID().toString());
		NWResponse clientResponse=new NWResponse(serviceMessage);
		//Send this request to the client. 
		Connection clientServiceConnection = new Connection(clientSocket);
		clientServiceConnection.send_response(clientResponse);
		//close client socket
		try {
			clientSocket.close();
		} catch (IOException e) {
			Constants.globalLog.debug("Erro while trying to close the socket");
			e.printStackTrace();
		}
		//Exit Thread
		
	}



	private synchronized String getIPAddress() {
		String IPAddress = null;
		try{

		if(turnOf > ConfigParams.getIpAddresses().size() - 1){
			turnOf = 0;
		}		
		IPAddress = ConfigParams.getIpAddresses().get(turnOf);
		turnOf++;
		}
		catch(Exception e){
			Constants.globalLog.debug("Error while trying to access the IP Addresses for scheduling");
			e.printStackTrace();
			
		}
		return IPAddress;
		
	}

}
