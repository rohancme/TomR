package edu.tomr.start;

import network.NetworkException;
import network.NetworkUtilities;
import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.utils.ConfigParams;

public class NodeStarter {
	
	private static int selfBeatPost = 5010;
	
	static {
		 ConfigParams.loadProperties();
	}
	
	private Node dbNode;
	private ClientBeatController beatController;
	
	public NodeStarter() {
		NetworkUtilities utils = null;
		try {
			utils = new NetworkUtilities();
			initDbNode(utils.getSelfIP());
			dbNode.initNetworkModule();
		} catch (NetworkException e) {
			
			System.out.println("Exception in obaining IP address");
			e.printStackTrace();
		}
		//beatController = new ClientBeatController(ConfigParams.getProperty("SERVER_IP"), 
			//	ConfigParams.getIntProperty("SERVER_PORT_NO"), utils.getSelfIP(), selfBeatPost);
		
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
	
	public static void main(String[] args) {
		
		NodeStarter nodeStarter = new NodeStarter();
		//nodeStarter.startBeatClient();
		
		
	}

}
