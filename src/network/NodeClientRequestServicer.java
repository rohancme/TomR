package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.node.base.Node;
import edu.tomr.protocol.DBMessage;
import network.requests.NWRequest;

public class NodeClientRequestServicer implements Runnable {
	
	Socket mySocket=null;
	Node myNode=null;
	ConcurrentHashMap<String, Socket> clientConnectionList=null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		NWRequest clientRequest=getSingleRequest();
		handleRequest(clientRequest);
	}
	
	public NodeClientRequestServicer(Socket socket,Node node, ConcurrentHashMap<String, Socket> clientConnectionList){
		this.mySocket=socket;
		this.myNode=node;
		this.clientConnectionList=clientConnectionList;
	}
	
	private void handleRequest(NWRequest request){
		//currently clients are only sending DBMessage. Don't see this changing
		DBMessage msg=request.getdBMessage();
		clientConnectionList.put(msg.getClientInfo().getIpAddress(), mySocket);
		myNode.handleRequest(msg);
	}
	
	private NWRequest getSingleRequest(){
		
		Scanner inputScanner=null;
		try {
			//not closing since if I close the scanner, it closes the inputStream, which closes the socket
			inputScanner = new Scanner(mySocket.getInputStream());
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
