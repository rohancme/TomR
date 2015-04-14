package edu.tomr.start;

import java.util.ArrayList;

import edu.tomr.protocol.StartupMessage;
import network.Connection;
import network.NetworkException;
import network.NetworkUtilities;
import network.requests.NWRequest;

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
		
		
		
		IPAddresses.add("192.168.1.131");
		
		IPAddresses.add("192.168.1.103");
		
		IPAddresses.add("192.168.1.138");
		
		//make the last one connectFirst
		int i;
		for(i=0;i<IPAddresses.size()-1;i++){
			ArrayList<String> neighbors=generateNeighborList(i,IPAddresses);
			StartupMessage msg=new StartupMessage("Testing123",neighbors, IPAddresses);
			NWRequest startupRequest=utils.getNewStartupRequest(msg);
			
			Connection temp_connection=new Connection(IPAddresses.get(i),startupMsgPort);
			
			temp_connection.send_request(startupRequest);

		}
		
		ArrayList<String> neighbors=generateNeighborList(i,IPAddresses);
		StartupMessage msg=new StartupMessage("Testing123",neighbors, IPAddresses, true);
		NWRequest startupRequest=utils.getNewStartupRequest(msg);
		
		Connection temp_connection=new Connection(IPAddresses.get(i),startupMsgPort);
		
		temp_connection.send_request(startupRequest);
		System.out.println("Connections sent");
		
	}

	private static ArrayList<String> generateNeighborList(int i,
			ArrayList<String> iPAddresses) {
		
		ArrayList<String> neighbors=new ArrayList<String>();
		
		if(i+1>iPAddresses.size()-1){
			neighbors.add(iPAddresses.get(0));
		}
		else{
			neighbors.add(iPAddresses.get(i+1));
		}
			
		
		return neighbors;
	}

}
