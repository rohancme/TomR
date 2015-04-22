package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import network.exception.NetworkException;
import network.requests.NWRequest;
import network.requests.incoming.NeighborConnectionHandler;
import network.requests.incoming.NodeCentralServerMessageHandler;
import network.requests.incoming.NodeClientRequestHandler;
import network.requests.incoming.StartupMessageHandler;
import network.requests.outgoing.NodeNeighborModule;
import network.responses.NWResponse;
import network.responses.incoming.NetworkResponseHandler;
import network.responses.outgoing.NodeResponseModule;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.node.base.Node;
import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.RedistributionMessage;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.utils.Constants;
//Main network module. An object of this is created on every Node in the cluster
public class NodeNetworkModule {
	
	private static int startupMsgPort=5000;
	private static int neighborServerPort=5001;
	private static int selfServerPort=5001;
	private static int responsePort=5002;
	private static int clientPort=5003;
	
	public NetworkUtilities utils=null;
	private NodeNeighborModule neighborModule=null;
	private NodeResponseModule responseModule=null;
	private final Node mainNodeObject;
	
	private ConcurrentHashMap<String,Socket> clientConnectionList=new ConcurrentHashMap<String,Socket>();
	
	/**
	 * @throws NetworkException
	 */
	public NodeNetworkModule(Node mainNodeObject) throws NetworkException{
		this.utils=new NetworkUtilities();
		this.mainNodeObject=mainNodeObject;
		constructorCommon();
	}
	
	
	/**
	 * @param selfIP-initialize NodeNetworkModule with a particular IP address
	 */
	public NodeNetworkModule(Node mainNodeObject,String selfIP){
		this.utils=new NetworkUtilities(selfIP);
		this.mainNodeObject=mainNodeObject;
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
		this.mainNodeObject.handleStartupRequest(startupRequest.getStartupMessage().getNodeList());
		this.neighborModule=setupNeighborConnections(startupRequest.getStartupMessage(),mainNodeObject);
		neighborModule.startServicingRequests();
		
		//everyone needs to start listening on port 5002 first
		NetworkResponseHandler incomingResponseHandler=null;
		try {
			incomingResponseHandler = new NetworkResponseHandler(responsePort,this,mainNodeObject);
		} catch (NetworkException e) {
					
			e.printStackTrace();
		}
		Thread incomingResponseThread=new Thread(incomingResponseHandler);
		incomingResponseThread.start();
				
		this.responseModule=new NodeResponseModule(startupRequest.getStartupMessage().getNeighborList(), responsePort);
		responseModule.startServicingResponses();
		
		//Now for the incoming client Connections
		NodeClientRequestHandler clientHandler=new NodeClientRequestHandler(clientPort,mainNodeObject,clientConnectionList);
		Thread incomingClientThread=new Thread(clientHandler);
		incomingClientThread.start();
		
		//Start listening for Server Messages
		NodeCentralServerMessageHandler serverHandler=new NodeCentralServerMessageHandler(NetworkConstants.C_SERVER_LISTEN_PORT,this.neighborModule,this.responseModule,this.utils,this.mainNodeObject);
		Thread serverHandlerThread=new Thread(serverHandler);
		serverHandlerThread.start();
	}
	
	/**
	 * @param msg-The outgoing DBMessage
	 * @param destIP-can't remember what this means
	 */
	public void sendOutgoingRequest(DBMessage msg,String destIP){
		NWRequest request=utils.getNewDBRequest(msg, destIP);
		this.neighborModule.insertOutgoingRequest(request);
	}
	
	public void sendOutgoingRequest(RedistributionMessage msg,String destIP){
		NWRequest request=utils.getNewRedisRequest(msg, destIP);
		this.neighborModule.insertOutgoingRequest(request);
	}
	
	/**
	 * @param request-Directly construct and send a NWRequest object.
	 * 				Currently isn't needed except by the incoming request handler thread 04/12/15
	 */
	public void sendOutgoingRequest(NWRequest request){
		this.neighborModule.insertOutgoingRequest(request);
	}
	
	
	public void sendOutgoingNWResponse(AckMessage message, String destIP){
		
		NWResponse response=new NWResponse(this.utils.getSelfIP(),destIP,message);
		this.responseModule.insertOutgoingNWResponse(response);
		
	}
	
	public void sendOutgoingNWResponse(NWResponse response){
		this.responseModule.insertOutgoingNWResponse(response);
	}
	
	//DUMMY-Waiting for ClientResponse Class
	public void sendOutgoingClientResponse(AckMessage message, String clientIPAddress){
		NWResponse response=new NWResponse(message);
		Socket clientSocket=clientConnectionList.get(clientIPAddress);
		sendResponse(clientSocket,response);
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constants.globalLog.error("Couldn't close the Client Socket");
		}
		
		
	}
	
	
	
	/********************************Private Methods********************************************************/
	
	private void constructorCommon(){
		//currently nothing
		
	}
	
	public void sendResponse(Socket socket,NWResponse response) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		DataOutputStream output_stream=null;
		try {
			output_stream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Constants.globalLog.debug("Unable to open output stream to host:"+socket.getInetAddress());
			e.printStackTrace();
			return;
		}
		
		try {
			//mapper.writeValue(System.out, request);
			mapper.writeValue(output_stream, response);
			//end of message marker.
			output_stream.writeChar('\n');
			output_stream.flush();
		} catch (JsonGenerationException e) {
			Constants.globalLog.debug("Problem Generating JSON");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			Constants.globalLog.debug("Problem with JSON mapping");
			e.printStackTrace();
		} catch (IOException e) {
			Constants.globalLog.debug("Problem with IO with host:"+socket.getInetAddress());
			e.printStackTrace();
		}
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
		try {
			myStartupHandler.shutdown();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		return startupRequest;
	}

	
	private NodeNeighborModule setupNeighborConnections(StartupMessage startupMessage, Node mainNodeObject) {
		
		//Use following:
		//NeighborConnection in order to establish a connection with neighbor
		//NeighborConnectionHandler in order to accept and continue receiving requests from a neighbor
		
		NodeNeighborModule neighborModule=null;
		
		boolean connectFirst=startupMessage.isConnectFirst();
		
		NeighborConnectionHandler incomingNeighborConnectionHandler = null;
		
		try {
			incomingNeighborConnectionHandler = new NeighborConnectionHandler(selfServerPort,this,mainNodeObject);
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
