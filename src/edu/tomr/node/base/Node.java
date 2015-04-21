package edu.tomr.node.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import network.NetworkUtilities;
import network.NodeNetworkModule;
import network.exception.NetworkException;
import edu.tomr.client.KeyValuePair;
import edu.tomr.hash.ConsistentHashing;
import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;
import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.ClientMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NodeMessage;
import edu.tomr.protocol.RedistributionMessage;
import edu.tomr.protocol.UpdateRingMessage;
import edu.tomr.queue.ClientQueueProcessor;
import edu.tomr.queue.MessageQueue;
import edu.tomr.queue.NodeQueueProcessor;
import edu.tomr.utils.ConfigParams;

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

	/* (non-Javadoc)
	 * @see edu.tomr.node.base.INode#handleRequest(edu.tomr.protocol.DBMessage)
	 */
	@Override
	public void handleRequest(DBMessage message) {

		clientInbox.queueMessage(new ClientMessage(message));
		if(null == clientProcThread){
			startClientProcessor();
		}
	}

	/* (non-Javadoc)
	 * @see edu.tomr.node.base.INode#handleRequest(edu.tomr.protocol.DBMessage, java.lang.String)
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

	/* (non-Javadoc)
	 * @see edu.tomr.node.base.INode#handleAcknowledgements(edu.tomr.protocol.AckMessage)
	 */
	@Override
	public void handleAcknowledgements(AckMessage ackMessage) {

		String clientIp = requestMapper.remove(ackMessage.getRequestIdServiced());
		networkModule.sendOutgoingClientResponse(ackMessage, clientIp);
	}

	/* (non-Javadoc)
	 * @see edu.tomr.node.base.INode#handleUpdateRingRequest(edu.tomr.protocol.UpdateRingMessage)
	 */
	@Override
	public void handleUpdateRingRequest(UpdateRingMessage message) {
		
		System.out.println("handling update ring requests in node: "+this.getSelfAddress());
		List<String> originalNodes = ConfigParams.getIpAddresses();
		originalNodes.add(message.getNewNode());

		ConsistentHashing.updateCircle(originalNodes);

		new Thread(new Runnable() {
		    @Override
			public void run() {
		    	try {
					redistributeKeys();
				} catch (NetworkException e) {
					
					e.printStackTrace();
				}
		    }
		}).start();
	}

	/* (non-Javadoc)
	 * @see edu.tomr.node.base.INode#redistributionRequest(edu.tomr.protocol.RedistributionMessage)
	 */
	@Override
	public void redistributionRequest(final RedistributionMessage message) {

		new Thread(new Runnable() {
		    @Override
			public void run() {

		    	addKeys(message);
			}
		}).start();
	}

	private void addKeys(RedistributionMessage message) {

		List<KeyValuePair> values = message.getKeys();

		for(KeyValuePair pair: values)
			operation.put(pair.getKey(), pair.getValue());
	}

	private void redistributeKeys() throws NetworkException {

		Map<String, List<String>> map = ConsistentHashing.redistributeKeys(inMemMap.keySet());//ConsistentHashing.redistributeKeys(null);
		NetworkUtilities utils = null;

		utils = new NetworkUtilities();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {

			if(! entry.getKey().equalsIgnoreCase(getSelfAddress())) {
				List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
				for(String key: entry.getValue()){
					KeyValuePair pair = new KeyValuePair(key, operation.get(key));
					pairs.add(pair);
					System.out.println("Move key: "+key+" to node: "+entry.getKey());
				}
				RedistributionMessage message = new RedistributionMessage(pairs);
				this.networkModule.sendOutgoingRequest(message, entry.getKey());
			}
		}
	}
	
	public void handleStartupRequest(List<String> nodeList) {
		
		ConfigParams.loadProperties(nodeList);
	}
}
