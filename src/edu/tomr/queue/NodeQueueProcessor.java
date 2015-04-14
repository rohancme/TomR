package edu.tomr.queue;

import edu.tomr.node.base.Node;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.protocol.AckMessage;
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
	
	//TODO: Send acknowledgments back to node
	public void handleMessage(DBMessage message) {
		
		ClientRequestPayload tempPayload = message.getPayload();
		byte[] val = null;
		boolean success = false;
		
		switch(message.getRequestType()) {
			case ADD: 	operation.put(tempPayload.getKey(), tempPayload.getValue());
						success = true;
						break;
						
			case GET:	
				System.out.println("Checking for key: "+tempPayload.getKey());
				val = operation.get(tempPayload.getKey());
				System.out.println("got value: "+val);
						if(val != null)
							success = true;
						break;
						
			case DELETE: operation.delete(tempPayload.getKey());
						 success = true;
						 break;
		
			case UPDATE: break;
			
		}
		AckMessage ack = new AckMessage(success, val, message.getRequestId());
		
		String ip = parentNode.getRequestMapper().remove(message.getRequestId());
		if(ip.equalsIgnoreCase(parentNode.getSelfAddress())){
			parentNode.getNetworkModule().sendOutgoingClientResponse(ack, message.getClientInfo().getIpAddress());
			//send ack to client using nw module
		} else {
			parentNode.getNetworkModule().sendOutgoingNWResponse(ack, ip);
			//send ack to ip using nw module
		}
	}
	 	
}
