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
	
	
	/*
	 * This method handles the requests meant for this node
	 * 
	 * @param message	message containing the KV pair from the load balancer	
	 * @return
	 */
	/*private void handleNodeRequest(NodeMessage message) {
		
		ClientRequestPayload tempPayload = message.getPayload();
		
		switch(message.getRequestType()) {
			case ADD: 	operation.put(tempPayload.getKey(), tempPayload.getValue());
						break;
						
			case GET:	tempPayload.setValue(operation.get(tempPayload.getKey()));
						break;
						
			case DELETE: operation.delete(tempPayload.getKey());
						 break;
		
			case UPDATE: break;
			
		}
		//Have to call da network module with request ack message
	}*/
		
}
