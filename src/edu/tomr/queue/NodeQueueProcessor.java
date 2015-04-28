package edu.tomr.queue;

import edu.tomr.node.base.Node;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NodeMessage;
import edu.tomr.replica.ValueUpdator.Operation;
import edu.tomr.utils.Constants;

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
					DBMessage msg = queue.dequeueMessage();
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
		byte[] val = null;
		boolean success = false;

		switch(message.getRequestType()) {
			case ADD: 	Constants.globalLog.debug("Hanlding put request for key: "+tempPayload.getKey()+" at node: "+parentNode.getSelfAddress());
						operation.put(tempPayload.getKey(), tempPayload.getValue());
						success = true;
						/*if(! parentNode.getReplicator().contains(tempPayload.getKey())) {
							parentNode.getReplicator().saveOperation(Operation.ADD, tempPayload.getKey());
						} else {

						}*/
						updateReplicator(Operation.ADD, tempPayload.getKey());

						break;

			case GET:	Constants.globalLog.debug("Hanlding get request for key: "+tempPayload.getKey()+" at node: "+parentNode.getSelfAddress());
						val = operation.get(tempPayload.getKey());
						if(val != null)
							success = true;
						break;

			case DELETE: operation.delete(tempPayload.getKey());
						 success = true;
						 updateReplicator(Operation.DELETE, tempPayload.getKey());
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

	private void updateReplicator(Operation oper, String key) {

		if(! parentNode.getReplicator().contains(key)) {
			parentNode.getReplicator().saveOperation(oper, key);
		} else {
			parentNode.getReplicator().updateOperation(oper, key);
		}
	}

}
