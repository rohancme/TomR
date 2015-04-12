package edu.tomr.protocol;

public class DBMessage extends Message {

	private final String requestId;
	private ClientInfo clientInfo;
	private final ClientRequestType requestType;
	private final ClientRequestPayload payload;
	
	public DBMessage(ClientRequestType type, ClientRequestPayload payload, ClientInfo info,
			String requestId) {
		
		this.requestType = type;
		this.payload = payload;
		this.clientInfo = info;
		this.requestId = requestId;
	}
	
	public ClientInfo getClientInfo() {
		return clientInfo;
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
