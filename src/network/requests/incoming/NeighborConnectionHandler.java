package network.requests.incoming;

import edu.tomr.node.base.Node;
import network.NetworkConstants;
import network.NodeNetworkModule;
import network.NetworkConstants.Requests;
import network.exception.NetworkException;
import network.incoming.persistent.RequestHandler;
import network.requests.NWRequest;
//handles incoming requests from neighbors
public class NeighborConnectionHandler extends RequestHandler implements Runnable{
	
	private NodeNetworkModule networkModule=null;
	private final Node mainNodeObject;

	@Override
	public void run() { //This needs to listen to incoming neighbor connections and requests
			
		//setup the connection
		try {
			initializeClientSocket();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		String ownIP=networkModule.utils.getSelfIP();
		//listen and handle all requests
		while(true){
			NWRequest request=getNextRequest();
			
			//this needs to be modified to call the relevant methods to handle the request
			if(request!=null){
				System.out.println("Received new request of type:"+request.getRequestType());
				
				if(ownIP.equals(request.getDestinationIP())){ //this request is meant for this node
					
					if(NetworkConstants.requestToString(Requests.DB_OPERATION).equals(request.getRequestType())){ //it's a DB Operation
						this.mainNodeObject.handleRequest(request.getdBMessage(), request.getSourceIP());
					}
					
					else if(NetworkConstants.requestToString(Requests.REDISTRIBUTION).equals(request.getRequestType())){
						System.out.println("Received a Redistribution request-NW level");
						this.mainNodeObject.redistributionRequest(request.getRedistributionMessage());
					}
					else if(NetworkConstants.requestToString(Requests.BREAK_INCOMING_CONNECTION).equals(request.getRequestType())){
						System.out.println("Request Conn-Received a request to break incoming neighbor Conn");
						changeIncomingNeighborConnection();
						System.out.println("Request Conn-A new neighbor has connected to me");
					}
				}
				else{ //either not meant for this node or null
					if(request.getDestinationIP()!=null){
						//put this request in the outgoing requests queue
						System.out.println("I want Nothing to do with this!");
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

}
