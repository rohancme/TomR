package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.requests.NWRequest;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class ConnectionHandler {
	
	protected ServerSocket serverSocket;
	protected Socket clientSocket;
	
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
	
	protected NWRequest getNextRequest(){
		
		ObjectMapper mapper = new ObjectMapper();
		NWRequest request=null;
		try {
			request=mapper.readValue(clientSocket.getInputStream(), NWRequest.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return request;
	}


}
