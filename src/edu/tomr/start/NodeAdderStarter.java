package edu.tomr.start;

import network.NetworkUtilities;
import network.exception.NetworkException;
import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.utils.ConfigParams;

@SuppressWarnings("unused")
public class NodeAdderStarter {

	private static int selfBeatPost = 5010;

	static {
		ConfigParams.loadProperties();
	}

	private Node dbNode;
	private ClientBeatController beatController;

	public NodeAdderStarter() {

		NetworkUtilities utils = null;
		try {
			utils = new NetworkUtilities();
			initDbNode(utils.getSelfIP());
			//TODO: Uncomment after NodeNetworkModule changes checked in
			//dbNode.initNetworkModule();
		} catch (NetworkException e) {

			System.out.println("Exception in obaining IP address");
			e.printStackTrace();
		}
		//beatController = new ClientBeatController(ConfigParams.getProperty("SERVER_IP"),
			//	ConfigParams.getIntProperty("SERVER_PORT_NO"), utils.getSelfIP(), selfBeatPost);
	}

	private void initDbNode(String ipAddress) {
		//this.dbNode = new Node(ipAddress);
	}

	private void sendAddMessageToLoadBalancer() {

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

		NodeAdderStarter starter = new NodeAdderStarter();
		//nodeStarter.startBeatClient();

	}

}
