package edu.tomr.protocol;

public class ClientMessage extends DBMessage {
	
	public ClientMessage(DBMessage message) {
		
		super(message.getRequestType(), message.getPayload(), message.getClientInfo(),
				message.getRequestId());
	}

}
