package edu.tomr.protocol;

public class ClientMessage extends DBMessage {
	
	/*public ClientMessage(ClientRequestType requestType, ClientRequestPayload payload,
			String reqestId) {
		
		super(requestType, payload, reqestId);
	}
	
	public ClientMessage() {
		
		super(null, null, null);
	}*/
	
	public ClientMessage(DBMessage message) {
		
		super(message.getRequestType(), message.getPayload(), message.getRequestId());
	}

}
