package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.requests.CloseRequest;
import network.requests.DBRequest;
import network.requests.NWRequest;
import network.requests.NewClientConnectionRequest;
import network.requests.NewNeighborConnectionRequest;
import network.requests.StartupRequest;

import org.codehaus.jackson.map.ObjectMapper;

public class ConnectionHandler {
	
	ServerSocket serverSocket;
	Socket clientSocket;
	
	public ConnectionHandler(int incoming_port) {
		
		try {
			serverSocket=new ServerSocket(incoming_port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void initializeClientSocket() throws NetworkException{
		try {
			clientSocket=serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException("Error accepting incoming connections");
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected NWRequest getNextRequest(){
		
		ObjectMapper mapper = new ObjectMapper();
		NetworkPacket packet=null;
		try {
			packet=mapper.readValue(clientSocket.getInputStream(),NetworkPacket.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return extractRequest(packet);
	}


	@SuppressWarnings("rawtypes")
	private NWRequest extractRequest(NetworkPacket packet) {
		
		switch(packet.type){
			
			case "NEW_NEIGHBOR_CONNECTION": NewNeighborConnectionRequest neighborRequest=(NewNeighborConnectionRequest) packet.request;
											return neighborRequest;
			
			case "DB_OPERATION": DBRequest dBRequest=(DBRequest) packet.request;
								 return dBRequest;
			
			case "STARTUP": StartupRequest startupRequest=(StartupRequest) packet.request;
							return startupRequest;
							
			case "CLOSE": CloseRequest closeRequest=(CloseRequest) packet.request;
						  return closeRequest;
						  
			case "NEW_CLIENT_CONNECTION": NewClientConnectionRequest newClientRequest=(NewClientConnectionRequest) packet.request;
										  return newClientRequest;
										  
			default: System.out.println("Invalid request. Currently Ignoring");
			
		}
		
		return null;
	}


}
