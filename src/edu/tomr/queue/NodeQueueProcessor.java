package edu.tomr.queue;

import edu.tomr.node.base.Node;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NodeMessage;

public class NodeQueueProcessor implements Runnable {

	private MessageQueue<NodeMessage> queue;
	IMapOperation operation;
	private final Node parentNode;
		
	public NodeQueueProcessor(MessageQueue<NodeMessage> queue, IMapOperation operation, 
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
	}	
	
}
