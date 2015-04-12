package edu.tomr.start;

import java.util.ArrayList;
import java.util.List;

import network.NeighborConnection;
import network.NeighborConnectionHandler;
import network.NetworkException;
import network.NetworkUtilities;
import network.StartupMessageHandler;
import network.requests.NWRequest;
import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.protocol.NeighborMessage;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.utils.ConfigParams;

public class NodeStarter {
	
	private static int selfBeatPost = 5010;
	
	static {
		 //ConfigParams.loadProperties();
	}
	
	private Node dbNode;
	private ClientBeatController beatController;
	
	public NodeStarter() {
		NetworkUtilities utils = null;
		List<NeighborConnection> neighborConnections = new ArrayList<NeighborConnection>();
		try {
			utils = new NetworkUtilities();
			initDbNode(utils.getSelfIP(), neighborConnections);
		} catch (NetworkException e) {
			
			System.out.println("Exception in obaining IP address");
			e.printStackTrace();
		}
		//beatController = new ClientBeatController(ConfigParams.getProperty("SERVER_IP"), 
			//	ConfigParams.getIntProperty("SERVER_PORT_NO"), utils.getSelfIP(), selfBeatPost);
		
	}
	
	
	private void initDbNode(String ipAddress, List<NeighborConnection> neighborConnections) {
		this.dbNode = new Node(ipAddress, neighborConnections);
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
	
	
	
	public static void main(String[] args) {
		
		NodeStarter nodeStarter = new NodeStarter();
		//nodeStarter.startBeatClient();
		nodeStarter.startNode();
		
		NetworkUtilities myUtils=null;
		try {
			myUtils=new NetworkUtilities();
		} catch (NetworkException e) {

			e.printStackTrace();
		}
		
		NeighborMessage msg=new NeighborMessage();

		NWRequest req=myUtils.getNewNeighborConnectionRequest(msg);
		
		for(NeighborConnection nc: nodeStarter.dbNode.getNeighbors()){
			nc.send_request(req);
		}
		
	}

}
