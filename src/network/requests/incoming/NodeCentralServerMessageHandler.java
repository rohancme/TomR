package network.requests.incoming;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.codehaus.jackson.map.ObjectMapper;

import network.NetworkConstants;
import network.NetworkConstants.Requests;
import network.NetworkUtilities;
import network.NodeNetworkModule;
import network.incoming.nonpersistent.NonPersistentIncomingConnectionHandler;
import network.outgoing.NeighborConnection;
import network.requests.NWRequest;
import network.requests.outgoing.NodeNeighborModule;
import edu.tomr.node.base.Node;
import edu.tomr.protocol.BreakIncomingNeighborConnectionMessage;
//handler for incoming central server requests
public class NodeCentralServerMessageHandler extends NonPersistentIncomingConnectionHandler implements Runnable{
	
	NodeNeighborModule neighborModule=null;
	NetworkUtilities utils=null;
	List<NeighborConnection> neighborConns=null;

	public NodeCentralServerMessageHandler(int incoming_port,NodeNeighborModule neighborModule,NetworkUtilities utils) {
		super(incoming_port);
		this.neighborModule=neighborModule;
		this.neighborConns=neighborModule.getOutgoingNeighborConnections();
		this.utils=utils;
	}
	
	@Override
	public void run() {
		while(true){
			Socket clientSocket=super.getNextSocket();
			
		}
	}
	
	private void handleConnection(Socket socket){
		NWRequest centralRequest=getSingleRequest(socket);
		
		if(centralRequest.getRequestType()==NetworkConstants.requestToString(Requests.BREAK_FORM)){ //it's a break message
			//generate a message of type break incoming neighbor connection
			BreakIncomingNeighborConnectionMessage msg=new BreakIncomingNeighborConnectionMessage("So long sucker");
			//generate a request with this message
			NWRequest outgoingRequest=new NWRequest(utils.generate_req_id(),msg);
			//send the request over the neighbor connection
			//currently only one neighbor
			NeighborConnection conn=null;
			for(NeighborConnection nCon:neighborConns){
				conn=nCon;
			}
			conn.send_request(outgoingRequest);
			//close the socket to neighbor
			conn.closeSocket();
			neighborConns.remove(conn);
			//to be sure
			neighborConns.clear();
			//get new neighbor's IP address
			ArrayList<String> newNeighbors=centralRequest.getBreakFormMessage().getNewNeighborList();
			for(String IP:newNeighbors){
				NeighborConnection newNeighborConnection=new NeighborConnection(IP, NetworkConstants.INCOMING_NEIGHBOR_PORT);
				neighborConns.add(newNeighborConnection);
			}
			//create a NeighborConnection with Neighbor
			//remove current connection in list
			//add new connection to list
		}
		
	}
	
	private NWRequest getSingleRequest(Socket socket){
		
		Scanner inputScanner=null;
		try {
			//not closing since if I close the scanner, it closes the inputStream, which closes the socket
			inputScanner = new Scanner(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		NWRequest request=null;
		//currently using scanner. Scanner waits for a newLine character which marks the end of an object
		System.out.println("Waiting for a message from the server");
		while(!inputScanner.hasNextLine());
		System.out.println("Got message from server");
		
		try {
			request=mapper.readValue(inputScanner.nextLine(), NWRequest.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//closing the socket
		inputScanner.close();
		
		return request;
	}

}

