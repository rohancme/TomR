package edu.tomr.node.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.hash.IConsistentHashing;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;
import edu.tomr.protocol.ClientMessage;
import edu.tomr.protocol.ClientRequestPayload;

/*
 * Should containing a network module to handle the connections
 * Can add appropriate constructors to initialize n/w module variables
 */
public class Node {
	
	private Map<String, byte[]> inMemMap;
	private IMapOperation operation;
	private final String selfIpAddress;
	
	public String getSelfAddress() {
		return this.selfIpAddress;
	}
	
	public Node(String selfIpAdd){
		
		this.selfIpAddress = selfIpAdd;
		inMemMap = new ConcurrentHashMap<String, byte[]>();
		operation = new MapOperation(inMemMap);
		
	}
	
	/**
	 * This method handles the client request received by the load balancer
	 * 
	 * @param	message			message containing the KV pair from the load balancer
	 * @return	isToForward  	whether the message has to be forwarded to the
	 * 							destination node
	 */
	public boolean handleClientRequest(ClientMessage message) {
		
		boolean isToForward;
		String ipAddress = ConsistentHashing.getNode(message.getPayload().getKey());
		message.setDestinationNode(ipAddress);
		
		if(ipAddress.equalsIgnoreCase(getSelfAddress())) {
			
			new Thread(new Runnable() {
			    public void run() {
			    	handleNodeRequest(message);
			    }
			}).start();
			isToForward = false;
		} else {
			message.setDestinationNode(ipAddress);
			isToForward = true;
		}
		
		return isToForward;
	}
	
	/**
	 * This method handles the requests meant for this node
	 * 
	 * @param message	message containing the KV pair from the load balancer	
	 * @return
	 */
	public ClientRequestPayload handleNodeRequest(ClientMessage message) {
		
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
		return tempPayload;
	}
	
}
