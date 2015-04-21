package network.responses.incoming;

import java.io.IOException;

import network.NodeNetworkModule;
import network.exception.NetworkException;
import network.incoming.persistent.PersistentIncomingConnectionHandler;
import network.responses.NWResponse;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.node.base.Node;
//handles incoming responses from neighbor nodes
public class NetworkResponseHandler extends PersistentIncomingConnectionHandler implements Runnable{

	public NetworkResponseHandler(int incoming_port) {
		super(incoming_port);
		// TODO Auto-generated constructor stub
	}
		
	private NodeNetworkModule networkModule=null;
	private Node currentNode;

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
			NWResponse response=getNextResponse();
			
			if(response!=null){
				System.out.println("Response recieved");
				
				if(ownIP.equals(response.getDestIP())){
					if(response.isResetIncomingResponseMsg()){
						System.out.println("Response Conn-Received a request to break incoming neighbor Conn");
						changeIncomingNeighborConnection();
						System.out.println("Response Conn-A new neighbor has connected to me");
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
