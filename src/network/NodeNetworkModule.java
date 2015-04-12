package network;

import network.requests.NWRequest;
import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.StartupMessage;

public class NodeNetworkModule {
	
	private static int startupMsgPort=5000;
	private static int neighborServerPort=5001;
	private static int selfServerPort=5001;
	
	public NetworkUtilities utils=null;
	private NodeNeighborModule neighborModule=null;
	
	
	/**
	 * @throws NetworkException
	 */
	public NodeNetworkModule() throws NetworkException{
		this.utils=new NetworkUtilities();
		constructorCommon();
	}
	
	
	/**
	 * @param selfIP-initialize NodeNetworkModule with a particular IP address
	 */
	public NodeNetworkModule(String selfIP){
		this.utils=new NetworkUtilities(selfIP);
		constructorCommon();
	}
	
	
	/**
	 * Gets the startup message.
	 * Sets up thread to listen to incoming connections.
	 * Initializes the neighborModule. This handles all outgoing connections and requests
	 * Starts the thread that starts servicing the outgoing request queue
	 */
	public void initializeNetworkFunctionality(){
		NWRequest startupRequest=getStartUpRequest(startupMsgPort);
		this.neighborModule=setupNeighborConnections(startupRequest.getStartupMessage());
		neighborModule.startServicingRequests();
	}
	
	/**
	 * @param msg-The outgoing DBMessage
	 * @param destIP-can't remember what this means
	 */
	public void sendOutgoingRequest(DBMessage msg,String destIP){
		NWRequest request=utils.getNewDBRequest(msg, destIP);
		this.neighborModule.insertOutgoingRequest(request);
	}
	
	/**
	 * @param request-Directly construct and send a NWRequest object.
	 * 				Currently isn't needed except by the incoming request handler thread 04/12/15
	 */
	public void sendOutgoingRequest(NWRequest request){
		this.neighborModule.insertOutgoingRequest(request);
	}
	
	//DUMMY-Waiting for Network Response Class
	public void sendOutgoingNWResponse(AckMessage message, String nodeIpAddress){
		
	}
	
	//DUMMY-Waiting for ClientResponse Class
	public void sendOutgoingClientResponse(AckMessage message, String clientIPAddress){
		
	}
	
	/********************************Private Methods********************************************************/
	
	private void constructorCommon(){
		//currently nothing
	}
	
	private NWRequest getStartUpRequest(int startUpMsgPort) {
		
		//1. Wait for the startup message
		StartupMessageHandler myStartupHandler=new StartupMessageHandler(startupMsgPort);
		NWRequest startupRequest=null;
		try {
			startupRequest=myStartupHandler.getRequest();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		return startupRequest;
	}

	
	private NodeNeighborModule setupNeighborConnections(StartupMessage startupMessage) {
		
		//Use following:
		//NeighborConnection in order to establish a connection with neighbor
		//NeighborConnectionHandler in order to accept and continue receiving requests from a neighbor
		
		NodeNeighborModule neighborModule=null;
		
		boolean connectFirst=startupMessage.isConnectFirst();
		
		NeighborConnectionHandler incomingNeighborConnectionHandler = null;
		
		try {
			incomingNeighborConnectionHandler = new NeighborConnectionHandler(selfServerPort,this);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		
		if(connectFirst){
			//first connect
			neighborModule=new NodeNeighborModule(startupMessage.getNeighborList(),neighborServerPort);
			//then listen
			Thread incomingConnectionsThread=new Thread(incomingNeighborConnectionHandler);
			incomingConnectionsThread.start();
		}
		else{
			//listen
			Thread incomingConnectionsThread=new Thread(incomingNeighborConnectionHandler);
			incomingConnectionsThread.start();
			//then connect
			neighborModule=new NodeNeighborModule(startupMessage.getNeighborList(),neighborServerPort);
			
		}
		
		return neighborModule;
		
	}


}
