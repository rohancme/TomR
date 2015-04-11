package edu.tomr.queue;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.protocol.ClientMessage;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NodeMessage;

public class QueueProcessor implements Runnable {

	private MessageQueue queue;
	IMapOperation operation;
	private final String nodeIpAddress;
	
	public QueueProcessor(MessageQueue queue, IMapOperation operation, String nodeIpAddress) {
		this.queue = queue;
		this.operation = operation;				
		this.nodeIpAddress = nodeIpAddress;
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
			
		if(message instanceof ClientMessage) {
			String ipAddress = ConsistentHashing.getNode(message.getPayload().getKey());

			if(ipAddress.equalsIgnoreCase(nodeIpAddress)) {
				handleNodeRequest((NodeMessage)message);
			} else {
				//TODO: Have to call da network module if forwarding required
			}
		}
		else if(message instanceof NodeMessage) {
			
			handleNodeRequest((NodeMessage)message);
		}
	}
	
	/*
	 * This method handles the requests meant for this node
	 * 
	 * @param message	message containing the KV pair from the load balancer	
	 * @return
	 */
	private void handleNodeRequest(NodeMessage message) {
		
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
	}
		
}
