package network.responses;

import java.net.Socket;

public class ClientResponseWrapper {
	
	private NWResponse response=null;
	private Socket socket=null;
	
	public ClientResponseWrapper(NWResponse response,Socket socket){
		this.response=response;
		this.socket=socket;
	}

	public NWResponse getResponse() {
		return response;
	}

	public Socket getSocket() {
		return socket;
	}

}
