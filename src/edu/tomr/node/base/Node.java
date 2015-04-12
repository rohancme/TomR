package edu.tomr.node.base;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import network.NeighborConnection;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;
import edu.tomr.protocol.ClientMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NodeMessage;
import edu.tomr.queue.ClientQueueProcessor;
import edu.tomr.queue.MessageQueue;
import edu.tomr.queue.NodeQueueProcessor;

/*
 * Should contain a network module to handle the connections
 * Can add appropriate constructors to initialize n/w module variables
 */
public class Node {
	
	
	/**
	 * Memory map to store the KV pair
	 */
	private Map<String, byte[]> inMemMap;
	
	/**
	 * Provides an API to operate on the inMemMap
	 */
	private IMapOperation operation;
	
	/**
	 * IP Address of the node
	 */
	private final String selfIpAddress;
	
	/**
	 * Messaging queue to store the messages to be serviced
	 */
	private MessageQueue<ClientMessage> clientInbox;
	
	/**
	 * Messaging queue to store the messages to be serviced
	 */
	private MessageQueue<NodeMessage> nodeInbox;
	
	/**
	 * List of neighbor connections that this node has
	 */
	private List<NeighborConnection> neighbors;
	
	/**
	 * Processor thread to service client queue messages 
	 */
	private Thread clientProcThread;
	
	/**
	 * Processor thread to service node queue messages 
	 */
	private Thread nodeProcThread;
	
	/**
	 * Primary constructor used to initialize the node
	 * @param selfIpAdd
	 * @param neigbors
	 */
	public Node(String selfIpAdd, List<NeighborConnection> neigbors){
		
		this.selfIpAddress = selfIpAdd;
		this.neighbors = neigbors;
		inMemMap = new ConcurrentHashMap<String, byte[]>();
		setOperation(new MapOperation(inMemMap));
		clientInbox = new MessageQueue<ClientMessage>();
		nodeInbox = new MessageQueue<NodeMessage>();
	}
		
	public void setNeighborConnection(NeighborConnection neighbor) {
		this.neighbors.add(neighbor);
	}
	
	public List<NeighborConnection> getNeighbors() {
		return neighbors;
	}

	public String getSelfAddress() {
		return this.selfIpAddress;
	}
	
	public IMapOperation getOperation() {
		return operation;
	}

	public void setOperation(IMapOperation operation) {
		this.operation = operation;
	}
	
	/**
	 * Start the processor thread to service messages
	 */
	private void startClientProcessor() {
		clientProcThread = new Thread(new ClientQueueProcessor(clientInbox, operation, 
				this));
		clientProcThread.start();
	}
	
	private void startNodeProcessor() {
		nodeProcThread = new Thread(new NodeQueueProcessor(nodeInbox, operation, 
				this));
		nodeProcThread.start();
	}
	
	public void handleRequest(DBMessage message, String originalServicerIP) {
				
		//Node message with original node IP 
		if(null != originalServicerIP) {
			nodeInbox.queueMessage(new NodeMessage(message, originalServicerIP));
			if(!nodeProcThread.isAlive())
				startNodeProcessor();
		} 
		//Client message
		else {
			clientInbox.queueMessage(new ClientMessage(message));
			if(!clientProcThread.isAlive())
				startClientProcessor();
		}
		
	}
	
}
