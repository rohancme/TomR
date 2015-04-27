package network.responses.incoming;

import java.io.IOException;

import network.NodeNetworkModule;
import network.exception.NetworkException;
import network.incoming.persistent.PersistentIncomingConnectionHandler;
import network.responses.NWResponse;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.node.base.Node;
import edu.tomr.utils.Constants;
//handles incoming responses from neighbor nodes
public class NetworkResponseHandler extends PersistentIncomingConnectionHandler implements Runnable{

	private NetworkResponseHandler(int incoming_port) {
		super(incoming_port);
		// TODO Auto-generated constructor stub
	}
		
	private NodeNetworkModule networkModule=null;
	private Node currentNode;
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
			NWResponse response=getNextResponse();
			
			if(response!=null){
				Constants.globalLog.debug("Response recieved");
				
				if(ownIP.equals(response.getDestIP())){
					if(response.isResetIncomingResponseMsg()){
						Constants.globalLog.debug("Response Conn-Received a request to break incoming neighbor Conn");
						changeIncomingNeighborConnection();
						Constants.globalLog.debug("Response Conn-A new neighbor has connected to me");
					}
					//call method to handle response
					else{
						currentNode.handleAcknowledgements(response.getAckMsg());
					}
				}
				else{
					//add to response queue
					networkModule.sendOutgoingNWResponse(response);
				}
			}
		}
	}
		
	public NetworkResponseHandler(int incoming_port,NodeNetworkModule module, Node currentNode) throws NetworkException{
		super(incoming_port);	
		this.networkModule=module;
		this.currentNode=currentNode;
	}
	
	public NetworkResponseHandler(int incoming_port,NodeNetworkModule module, Node currentNode,boolean sendInit) throws NetworkException{
		super(incoming_port);	
		this.networkModule=module;
		this.currentNode=currentNode;
		this.sendInitialResponse=sendInit;
	}
	
	protected NWResponse getNextResponse(){
		
		ObjectMapper mapper = new ObjectMapper();
		NWResponse response=null;
		//currently using scanner. Scanner waits for a newLine character which marks the end of an object
		if(inputScanner.hasNextLine()){
			try {
				response=(NWResponse) mapper.readValue(inputScanner.nextLine(), NWResponse.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return response;
	}


}
