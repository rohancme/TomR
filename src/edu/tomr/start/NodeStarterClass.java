package edu.tomr.start;

import java.util.ArrayList;

import edu.tomr.protocol.StartupMessage;
import network.NeighborConnection;
import network.NeighborConnectionHandler;
import network.NetworkException;
import network.StartupMessageHandler;
import network.requests.NWRequest;

public class NodeStarterClass {
	
	static int startupMsgPort=5000;
	static int neighborServerPort=5001;
	static int selfServerPort=5002;
	

	public static void main(String[] args) {
		
		//1. Wait for the startup message
		StartupMessageHandler myStartupHandler=new StartupMessageHandler(startupMsgPort);
		NWRequest startupRequest=null;
		try {
			startupRequest=myStartupHandler.getRequest();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		//Extract information about who to connect with and in what order
		StartupMessage startupMessage=(StartupMessage) startupRequest.getStartupMessage();
		//Use following:
		//NeighborConnection in order to establish a connection with neighbor
		//NeighborConnectionHandler in order to accept and continue receiving requests from a neighbor
		
		boolean connectFirst=startupMessage.isConnectFirst();
		
		ArrayList<NeighborConnection> neighborConnections=new ArrayList<NeighborConnection>();
		NeighborConnectionHandler incomingNeighborConnectionHandler = null;
		try {
			incomingNeighborConnectionHandler = new NeighborConnectionHandler(selfServerPort);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		
		if(connectFirst){
			//first connect
			for(String neighborIP:startupMessage.getNeighborList()){
				NeighborConnection connection=new NeighborConnection(neighborIP,neighborServerPort);
				neighborConnections.add(connection);
			}
			//then listen
			Thread incomingConnectionsThread=new Thread(incomingNeighborConnectionHandler);
			incomingConnectionsThread.start();
		}
		else{
			//listen
			Thread incomingConnectionsThread=new Thread(incomingNeighborConnectionHandler);
			incomingConnectionsThread.start();
			//then connect
			for(String neighborIP:startupMessage.getNeighborList()){
				NeighborConnection connection=new NeighborConnection(neighborIP,neighborServerPort);
				neighborConnections.add(connection);
			}
			
		}
		
		
		//Still need to implement listening for Client Requests	
		
		

	}

}
