package edu.tomr.start;

import network.NetworkUtilities;
import network.exception.NetworkException;
import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.utils.Constants;

public class NodeStarter {

	//private static int selfBeatPost = 5010;

	static {
		 //ConfigParams.loadProperties();
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

			Constants.globalLog.debug("Exception in obaining IP address");
			e.printStackTrace();
		}
		//beatController = new ClientBeatController(ConfigParams.getProperty("SERVER_IP"),
				//	ConfigParams.getIntProperty("SERVER_PORT_NO"), utils.getSelfIP(), NetworkConstants.C_BEAT_PORT);

	}


	private void initDbNode(String ipAddress) {
		this.dbNode = new Node(ipAddress);
	}

	private void startBeatClient() {
		try {
			beatController.startHeartBeats();
		} catch (InterruptedException e) {

			Constants.globalLog.debug("Exception in client heart beat module");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		NodeStarter nodeStarter = new NodeStarter();
		//nodeStarter.startBeatClient();

	}

}
