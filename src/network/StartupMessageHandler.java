package network;

import network.requests.StartupRequest;

//don't think this needs to be a separate thread currently
public class StartupMessageHandler extends ConnectionHandler{

	public StartupMessageHandler(int incoming_port) {
		super(incoming_port);
	}
	
	public StartupRequest getRequest() throws NetworkException{
		initializeClientSocket();
		return (StartupRequest) getNextRequest();
	}

}
