package network.requests.incoming;

import network.NetworkConstants;
import network.NetworkConstants.Requests;
import network.NodeNetworkModule;
import network.exception.NetworkException;
import network.incoming.persistent.RequestHandler;
import network.requests.NWRequest;
import network.responses.NWResponse;
import edu.tomr.node.base.Node;
import edu.tomr.utils.Constants;
//handles incoming requests from neighbors
public class NeighborConnectionHandler extends RequestHandler implements Runnable{
	
	private NodeNetworkModule networkModule=null;
	private final Node mainNodeObject;
	private boolean sendInitialResponse=false;

	@Override
	public void run() { //This needs to listen to incoming neighbor connections and requests
			
		//setup the connection
		try {
			initializeClientSocket();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		String ownIP=networkModule.utils.getSelfIP();
		
		if(sendInitialResponse){
			NWResponse incomingNeighborResponse=new NWResponse(ownIP,null);
			super.sendOutgoingResponse(clientSocket, incomingNeighborResponse);
		}
				
		//listen and handle all requests
		while(true){
			NWRequest request=getNextRequest();
			
			//this needs to be modified to call the relevant methods to handle the request
			if(request!=null){
				Constants.globalLog.debug("Received new request of type:"+request.getRequestType());
				
				if(ownIP.equals(request.getDestinationIP())){ //this request is meant for this node
					
					if(NetworkConstants.requestToString(Requests.DB_OPERATION).equals(request.getRequestType())){ //it's a DB Operation
						this.mainNodeObject.handleRequest(request.getdBMessage(), request.getSourceIP());
					}
					
					else if(NetworkConstants.requestToString(Requests.REDISTRIBUTION).equals(request.getRequestType())){
						Constants.globalLog.debug("Received a Redistribution request-NW level");
						this.mainNodeObject.redistributionRequest(request.getRedistributionMessage());
					}
					else if(NetworkConstants.requestToString(Requests.BREAK_INCOMING_CONNECTION).equals(request.getRequestType())){
						Constants.globalLog.debug("Request Conn-Received a request to break incoming neighbor Conn");
						changeIncomingNeighborConnection();
						Constants.globalLog.debug("Request Conn-A new neighbor has connected to me");
					}
				}
				else{ //either not meant for this node or null
					if(request.getDestinationIP()!=null){
						//put this request in the outgoing requests queue
						Constants.globalLog.debug("I want Nothing to do with this!");
						networkModule.sendOutgoingRequest(request);
					}
					else{ //some other kind of request that may be used in the future
						
					}
				}
			}
		}
		
	}
	
	
	public NeighborConnectionHandler(int incoming_port,NodeNetworkModule module, Node mainNodeObject) throws NetworkException{
		super(incoming_port);	
		this.networkModule=module;
		this.mainNodeObject=mainNodeObject;
	}
	
	public NeighborConnectionHandler(int incoming_port,NodeNetworkModule module, Node mainNodeObject,boolean sendInit) throws NetworkException{
		super(incoming_port);	
		this.networkModule=module;
		this.mainNodeObject=mainNodeObject;
		this.sendInitialResponse=sendInit;
	}

}
