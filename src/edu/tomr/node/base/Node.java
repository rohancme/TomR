package edu.tomr.node.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import network.NodeNetworkModule;
import network.exception.NetworkException;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;
import edu.tomr.protocol.AckMessage;
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
public class Node implements INode {


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
	 * Processor thread to service client queue messages
	 */
	private Thread clientProcThread;

	/**
	 * Processor thread to service node queue messages
	 */
	private Thread nodeProcThread;

	/**
	 * NodeNetworkModule handler
	 */
	private NodeNetworkModule networkModule;

	/**
	 * Map to store the RequestID and IP Address
	 */
	private Map<String, String> requestMapper;


	public Map<String, String> getRequestMapper() {
		return requestMapper;
	}

	/**
	 * Primary constructor used to initialize the node
	 * @param selfIpAdd
	 * @param neigbors
	 */
	public Node(String selfIpAdd){

		this.selfIpAddress = selfIpAdd;
		//initNetworkModule();
		inMemMap = new ConcurrentHashMap<String, byte[]>();
		setOperation(new MapOperation(inMemMap));
		clientInbox = new MessageQueue<ClientMessage>();
		nodeInbox = new MessageQueue<NodeMessage>();
		requestMapper = new HashMap<String, String>();
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

	public NodeNetworkModule getNetworkModule() {
		return networkModule;
	}

	public void initNetworkModule(){
		try {
			this.networkModule = new NodeNetworkModule(this);
		} catch (NetworkException e) {
			System.out.println("Error while instantiating network module");
			e.printStackTrace();
		}
		this.networkModule.initializeNetworkFunctionality();
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

	/**
	 * For client requests
	 * @param message DBMessage from client
	 */
	@Override
	public void handleRequest(DBMessage message) {

		clientInbox.queueMessage(new ClientMessage(message));
		if(null == clientProcThread){
			startClientProcessor();
		}
	}

	/**
	 * For node requests
	 * @param message DBMessage from client
	 * @param originalServicerIP : Original node IP Address servicing the request
	 */
	@Override
	public void handleRequest(DBMessage message, String originalServicerIP) {

		requestMapper.put(message.getRequestId(), originalServicerIP);
		if(null != originalServicerIP) {
			nodeInbox.queueMessage(new NodeMessage(message, originalServicerIP));
			if(null == nodeProcThread)//.isAlive())
				startNodeProcessor();
		}
	}

	/**
	 * Only for node acknowledgments
	 * @param ackMessage
	 */
	@Override
	public void handleAcknowledgements(AckMessage ackMessage) {

		String clientIp = requestMapper.remove(ackMessage.getRequestIdServiced());
		networkModule.sendOutgoingClientResponse(ackMessage, clientIp);
	}

}
