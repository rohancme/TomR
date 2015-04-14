package edu.tomr.start;

import edu.tomr.network.heartbeat.client.ClientBeatController;
import edu.tomr.node.base.Node;
import edu.tomr.utils.ConfigParams;

public class NodeAdder {

	static {
		 ConfigParams.loadProperties();
	}
	
	private Node dbNode;
	@SuppressWarnings("unused")
	private ClientBeatController beatController;
	
	
}
