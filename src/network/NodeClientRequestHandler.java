package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import network.requests.NWRequest;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.node.base.Node;

public class NodeClientRequestHandler extends
		NonPersistentIncomingConnectionHandler {
	
	Node currentNode=null;
	ConcurrentHashMap<String,Socket> clientConnectionList=new ConcurrentHashMap<String,Socket>();

	protected NodeClientRequestHandler(int incoming_port,Node node) {
		super(incoming_port);
		this.currentNode=node;
	}
	
	void startServicingClientRequests(){
		Socket clientSocket=super.getNextSocket();
		clientConnectionList.put(clientSocket.getInetAddress().getHostAddress(), clientSocket);
	}
	
	protected NWRequest getSingleRequest(Socket clientSocket){
		
		Scanner inputScanner=null;
		try {
			inputScanner = new Scanner(clientSocket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		NWRequest request=null;
		//currently using scanner. Scanner waits for a newLine character which marks the end of an object
		if(inputScanner.hasNextLine()){
			try {
				request=mapper.readValue(inputScanner.nextLine(), NWRequest.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return request;
	}

}
