package edu.tomr.start;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import network.NetworkException;
import network.NetworkUtilities;
import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.protocol.ClientInfo;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.ClientRequestType;
import edu.tomr.protocol.DBMessage;
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
		
		Map<String, byte[]> testMap = new HashMap<String, byte[]>();
		//192.168.1.131
		testMap.put("File-6", "File-6".getBytes());
		testMap.put("File-4", "File-4".getBytes());
		testMap.put("File-2", "File-2".getBytes());
		testMap.put("File-12", "File-12".getBytes());
		testMap.put("File-13", "File-13".getBytes());
		testMap.put("File-10", "File-10".getBytes());
		testMap.put("File-15", "File-15".getBytes());
		
		//192.168.1.103
		testMap.put("File-5", "File-5".getBytes());
		testMap.put("File-3", "File-3".getBytes());
		testMap.put("File-11", "File-11".getBytes());
		
		//192.168.1.138
		testMap.put("File-1", "File-1".getBytes());
		testMap.put("File-7", "File-7".getBytes());
		testMap.put("File-8", "File-8".getBytes());
		testMap.put("File-9", "File-9".getBytes());
		testMap.put("File-14", "File-14".getBytes());
		
		nodeStarter.dbNode.setInMemMap(testMap);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ClientInfo new ClientInfo("1.2.3.40") = new Clientnew ClientInfo("1.2.3.40")("1.2.3.40");
		
		/*
		 * Same node reqs
		 */
		//192.168.1.131
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-6"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-10"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-15"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		
		//192.168.1.103
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-5"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-3"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-11"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		
		//192.168.1.138
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-14"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-1"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-7"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		
		/*
		 * Diff node reqs
		 */
		//192.168.1.131
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-5"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-3"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-11"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		
		//192.168.1.103
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-6"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-14"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-7"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		
		//192.168.1.138
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-6"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-10"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
		nodeStarter.dbNode.handleRequest(new DBMessage(ClientRequestType.GET, new ClientRequestPayload("File-11"), new ClientInfo("1.2.3.40"), 
				UUID.randomUUID().toString()));
	}

}
