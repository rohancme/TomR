package edu.tomr.protocol;

public class DBMessage extends Message {

	private final String requestId;
	private final ClientRequestType requestType;
	private final ClientRequestPayload payload;
	
	public DBMessage(ClientRequestType type, ClientRequestPayload payload, String requestId) {
		
		this.requestType = type;
		this.payload = payload;
		this.requestId = requestId;
	}
	
	public ClientRequestPayload getPayload() {
		return payload;
	}

	public ClientRequestType getRequestType() {
		return requestType;
	}

	public String getRequestId() {
		return requestId;
	}
	
}
