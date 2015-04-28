package edu.tomr.loadbalancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import network.Connection;
import network.NetworkConstants;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;
import network.requests.incoming.LBClientServer;
import edu.tomr.handler.AddMessageHandler;
import edu.tomr.handler.PortRequestHandler;
import edu.tomr.network.heartbeat.server.ServerBeatController;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.utils.ConfigParams;

public class LoadBalancer {

	static {
		 ConfigParams.loadProperties();
	}

	static int startupMsgPort=5000;

	static int sizeOfThreadPool=20;
	private ServerBeatController beatController;

	Map<String, List<String>> replicMap;

	public LoadBalancer() {
		beatController = new ServerBeatController();
	}

	public void startHeartBeatServer() {
		beatController.start();
	}

	public void startServer() {

		NetworkUtilities utils = null;

		try {
			utils = new NetworkUtilities();
		} catch (NetworkException e) {
			e.printStackTrace();
		}

		List<String> nodeAddresses = ConfigParams.getIpAddresses();

		//make the last one connectFirst
		int i;
		for(i=0;i<nodeAddresses.size()-1;i++){

			List<String> neighbors=generateNeighborList(i,nodeAddresses);
			StartupMessage msg=new StartupMessage("Testing123", neighbors, nodeAddresses);
			NWRequest startupRequest=utils.getNewStartupRequest(msg);

			Connection temp_connection=new Connection(nodeAddresses.get(i),startupMsgPort);

			temp_connection.send_request(startupRequest);

		}

		List<String> neighbors = generateNeighborList(i,nodeAddresses);
		StartupMessage msg = new StartupMessage("Testing123", neighbors, nodeAddresses, true);
		NWRequest startupRequest = utils.getNewStartupRequest(msg);

		Connection temp_connection=new Connection(nodeAddresses.get(i),startupMsgPort);

		temp_connection.send_request(startupRequest);
	}

	private static List<String> generateNeighborList(int i, List<String> iPAddresses) {

		List<String> neighbors=new ArrayList<String>();

		if(i+1>iPAddresses.size()-1){
			neighbors.add(iPAddresses.get(0));
		}
		else{
			neighbors.add(iPAddresses.get(i+1));
		}

		return neighbors;
	}

	private void listenForClients() {

		@SuppressWarnings("unused")
		LBClientServer IPServer = new LBClientServer();
	}

	private void startServicingPorts() {

		//Start servicing add message port LB_ADD_LISTEN_PORT
		PortRequestHandler<AddMessageHandler> addMsgHandler = new PortRequestHandler<AddMessageHandler>(NetworkConstants.LB_ADD_LISTEN_PORT,
				AddMessageHandler.class.getName());
		Thread addMsgThread = new Thread(addMsgHandler);
		addMsgThread.start();

	}

	private void initReplicaNodes() {

		this.replicMap = new HashMap<String, List<String>>();
		List<String> replicas = ConfigParams.getReplicaAddresses();
		List<String> nodes  = ConfigParams.getIpAddresses();

		int x = 0, factor = ConfigParams.getIntProperty("REPLICATION_FACTOR");

		for(int i =0; i<ConfigParams.getIpAddresses().size(); i++) {
			List<String> temp = new ArrayList<String>();
			int index = factor*i;
			for(int j=0; j<factor; j++){
				temp.add(replicas.get(index));
				index++;
			}
			this.replicMap.put(nodes.get(x), temp);
			x++;
		}

		NetworkUtilities utils = null;

		try {
			utils = new NetworkUtilities();
		} catch (NetworkException e) {
			e.printStackTrace();
		}

		for(Entry<String, List<String>> entry: this.replicMap.entrySet()){

			StartupMessage msg = new StartupMessage(true, "REPLICA_STARTUP", entry.getKey(), null);
			//StartupMessage("Testing123", neighbors, nodeAddresses);
			NWRequest startupRequest=utils.getNewStartupRequest(msg);

			Connection temp_connection=new Connection(entry.getKey(), startupMsgPort);

			temp_connection.send_request(startupRequest);

		}
	}

	public static void main(String[] args) {

		LoadBalancer loadBalancer = new LoadBalancer();
		//loadBalancer.startHeartBeatServer();
		loadBalancer.startServer();
		loadBalancer.listenForClients();
		loadBalancer.startServicingPorts();
		loadBalancer.initReplicaNodes();
	}


}
