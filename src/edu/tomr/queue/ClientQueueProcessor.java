package edu.tomr.queue;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.node.base.Node;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.protocol.ClientMessage;
import edu.tomr.protocol.DBMessage;

public class ClientQueueProcessor implements Runnable {

	private MessageQueue<ClientMessage> queue;
	IMapOperation operation;
	private final Node parentNode;
		
	public ClientQueueProcessor(MessageQueue<ClientMessage> queue, IMapOperation operation, 
			Node parentNode) {
		this.queue = queue;
		this.operation = operation;				
		this.parentNode = parentNode;
	}
	
	@Override
	public void run() {
		
		boolean running = true;

		while(running) {
			try {
				Thread.sleep(50);
				if(!queue.isEmpty()){
					DBMessage msg = (DBMessage)queue.dequeueMessage();
					handleMessage(msg);
				}
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
		}
	}
	
	
	public void handleMessage(DBMessage message) {
		
		String ipAddress = ConsistentHashing.getNode(message.getPayload().getKey());

		if(ipAddress.equalsIgnoreCase(parentNode.getSelfAddress())) {
			parentNode.getRequestMapper().put(message.getRequestId(), message.getClientInfo().getIpAddress());
			parentNode.handleRequest(message, parentNode.getSelfAddress());
		} else {
			parentNode.getRequestMapper().put(message.getRequestId(), message.getClientInfo().getIpAddress());
			parentNode.getNetworkModule().sendOutgoingRequest(message, ipAddress);
		}
	}
	
}
