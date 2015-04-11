package edu.tomr.node.base;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import network.NeighborConnection;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;
import edu.tomr.protocol.DBMessage;
import edu.tomr.queue.MessageQueue;
import edu.tomr.queue.QueueProcessor;

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
	private MessageQueue inbox;
	
	/**
	 * List of neighbor connections that this node has
	 */
	private List<NeighborConnection> neighbors;
	
	/**
	 * Processor thread to service queue messages 
	 */
	private Thread procThread;
	
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
		inbox = new MessageQueue();
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
	private void startProcessor() {
		procThread = new Thread(new QueueProcessor(inbox, operation, getSelfAddress()));
		procThread.start();
	}
	
	public void handleRequest(DBMessage message) {
		//Add to queue and return
		inbox.queueMessage(message);
		if(!procThread.isAlive())
			startProcessor();
	}

	
}
