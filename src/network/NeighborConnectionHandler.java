package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.protocol.Message;
import network.requests.DBRequest;
import network.requests.NWRequest;
import network.requests.NewNeighborConnectionRequest;

public class NeighborConnectionHandler implements Runnable{
	
	ServerSocket serverSocket;
	Socket clientSocket;

	@Override
	public void run() { //This needs to listen to incoming neighbor connections and requests
		
		
		//setup the connection
		try {
			initializeClientSocket();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			NWRequest request=getNextRequest();
			System.out.println("Received new request of type:"+request.getRequestType());
		}
		
	}
	
	
	public NeighborConnectionHandler(int incoming_port) throws NetworkException{
		
		try {
			serverSocket=new ServerSocket(incoming_port);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	private void initializeClientSocket() throws NetworkException{
		try {
			clientSocket=serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException("Error accepting incoming connections");
		}
	}
	
	private NWRequest getNextRequest(){
		
		Scanner sc=null;
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


	private NWRequest extractRequest(NetworkPacket packet) {
		
		switch(packet.type){
			
			case "NEW_NEIGHBOR_CONNECTION": NewNeighborConnectionRequest neighborRequest=(NewNeighborConnectionRequest) packet.request;
											return neighborRequest;
			
			case "DB_OPERATION": DBRequest dBRequest=(DBRequest) packet.request;
								 return dBRequest;
				
			default: System.out.println("Invalid request. Currently Ignoring");
			
		}
		
		return null;
	}
	
	
	

}
