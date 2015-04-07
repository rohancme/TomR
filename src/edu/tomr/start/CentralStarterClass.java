package edu.tomr.start;

import java.util.ArrayList;

import edu.tomr.protocol.StartupMessage;
import network.Connection;
import network.NetworkException;
import network.NetworkPacket;
import network.NetworkUtilities;
import network.requests.StartupRequest;

public class CentralStarterClass {
	
	static int startupMsgPort=5000;
	
	public static void main(String args[]){
		
		NetworkUtilities utils=null;
		
		try {
			utils=new NetworkUtilities();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		//Need some way to get this initial list. Properties object? Config File?
		
		ArrayList<String> IPAddresses=new ArrayList<String>();
		
		IPAddresses.add("");
		IPAddresses.add("");
		IPAddresses.add("");
		
		//make the last one connectFirst
		for(int i=0;i<IPAddresses.size()-1;i++){
			ArrayList<String> neighbors=generateNeighborList(i,IPAddresses);
			StartupMessage msg=new StartupMessage("Testing123",neighbors);
			NetworkPacket<StartupRequest> packet=utils.getNewStartupRequest(msg);
			
			Connection temp_connection=new Connection(IPAddresses.get(i),startupMsgPort);
			
			temp_connection.send_request(packet);

		}
	}

	private static ArrayList<String> generateNeighborList(int i,
			ArrayList<String> iPAddresses) {
		
		ArrayList<String> neighbors=new ArrayList<String>();
		
		if(i+1>iPAddresses.size()-1){
			neighbors.add(iPAddresses.get(0));
		}
		else{
			neighbors.add(iPAddresses.get(i));
		}
			
		
		return neighbors;
	}

}
