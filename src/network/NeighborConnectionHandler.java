package network;

import network.requests.NWRequest;

public class NeighborConnectionHandler extends ConnectionHandler implements Runnable{

	@Override
	public void run() { //This needs to listen to incoming neighbor connections and requests
			
		//setup the connection
		try {
			initializeClientSocket();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		//listen and handle all requests
		while(true){
			NWRequest request=getNextRequest();
			
			//this needs to be modified to call the relevant methods to handle the request
			if(request!=null)
				System.out.println("Received new request of type:"+request.getRequestType());
		}
		
	}
	
	
	public NeighborConnectionHandler(int incoming_port) throws NetworkException{
		super(incoming_port);
		
	}

}
