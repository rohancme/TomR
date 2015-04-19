package network.requests.incoming;

import network.exception.NetworkException;
import network.incoming.persistent.RequestHandler;
import network.requests.NWRequest;

//don't think this needs to be a separate thread currently
public class StartupMessageHandler extends RequestHandler{

	public StartupMessageHandler(int incoming_port) {
		super(incoming_port);
	}
	
	public NWRequest getRequest() throws NetworkException{
		initializeClientSocket();
		return getNextRequest();
	}

}
