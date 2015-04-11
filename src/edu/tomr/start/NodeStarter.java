package edu.tomr.start;

import java.util.ArrayList;

import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.utils.ConfigParams;
import network.NeighborConnection;
import network.NeighborConnectionHandler;
import network.NetworkException;
import network.NetworkUtilities;
import network.StartupMessageHandler;
import network.requests.NWRequest;

//TODO: Still need to implement listening for Client Requests	
public class NodeStarterClass {
	
	static int startupMsgPort=5000;
	static int neighborServerPort=5001;
	static int selfServerPort=5002;
	static int selfBeatPost = 5010;
	
	static {
		 ConfigParams.loadProperties();
	}
	
	private Node dbNode;
	private ArrayList<NeighborConnection> neighborConnections;
	
	private ClientBeatController beatController;
	
	public NodeStarterClass() {
		NetworkUtilities utils = null;
		this.neighborConnections = new ArrayList<NeighborConnection>();
		try {
			utils = new NetworkUtilities();
			initDbNode(utils.getSelfIP());
		} catch (NetworkException e) {
			
			System.out.println("Exception in obaining IP address");
			e.printStackTrace();
		}
		beatController = new ClientBeatController(ConfigParams.getProperty("SERVER_IP"), 
				ConfigParams.getIntProperty("SERVER_PORT_NO"), utils.getSelfIP(), selfBeatPost);
		
	}
	
	
	private void initDbNode(String ipAddress) {
		this.dbNode = new Node(ipAddress);
	}
	
	private void startBeatClient() {
		try {
			beatController.startHeartBeats();
		} catch (InterruptedException e) {
			
			System.out.println("Exception in client heart beat module");
			e.printStackTrace();
		}
	}
	
	public void startNode() {
		
		NWRequest startUpRequest = getStartUpRequest(startupMsgPort);
		handleNeighborConnections(startUpRequest);
	}
	
	private NWRequest getStartUpRequest(int startUpMsgPort) {
		
		//1. Wait for the startup message
		StartupMessageHandler myStartupHandler=new StartupMessageHandler(startupMsgPort);
		NWRequest startupRequest=null;
		try {
			startupRequest=myStartupHandler.getRequest();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		return startupRequest;
	}

	private void handleNeighborConnections(NWRequest startUpRequest) {
		
		//Extract information about who to connect with and in what order
		StartupMessage startupMessage=(StartupMessage) startUpRequest.getStartupMessage();
		//Use following:
		//NeighborConnection in order to establish a connection with neighbor
		//NeighborConnectionHandler in order to accept and continue receiving requests from a neighbor
		
		boolean connectFirst=startupMessage.isConnectFirst();
		
		//neighborConnections = new ArrayList<NeighborConnection>();
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
		
	}
	
	public static void main(String[] args) {
		
		NodeStarterClass nodeStarter = new NodeStarterClass();
		//nodeStarter.startBeatClient();
		nodeStarter.startNode();
		
	}

}
