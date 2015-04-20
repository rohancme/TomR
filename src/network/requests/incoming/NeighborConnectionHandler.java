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
				
					String reqType=NetworkConstants.requestToString(Requests.DB_OPERATION);
					
					if(reqType.equals(request.getRequestType())){ //it's a DB Operation
						this.mainNodeObject.handleRequest(request.getdBMessage(), request.getSourceIP());
					}
				}
				else{ //either not meant for this node or null
					if(request.getDestinationIP()!=null){
						//put this request in the outgoing requests queue
						System.out.println("I want Nothing to do with this!");
						networkModule.sendOutgoingRequest(request);
					}
					else{ //some other kind of request that may be used in the future
						if(NetworkConstants.requestToString(Requests.BREAK_INCOMING_CONNECTION).equals(request.getRequestType())){
							changeIncomingNeighborConnection();
						}
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
	
	public void changeIncomingNeighborConnection(){
		try {
			closeClientSocket();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Closed incoming neighbor connection");
		try {
			initializeClientSocket();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Accepted new incoming neighbor connection");
	}

}
