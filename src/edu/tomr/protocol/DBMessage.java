package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class DBMessage extends Message {

	@JsonProperty private final String requestId;
	@JsonProperty private ClientInfo clientInfo;
	@JsonProperty private final ClientRequestType requestType;
	@JsonProperty private final ClientRequestPayload payload;

	public DBMessage(){
		this.requestType = null;
		this.payload = null;
		this.requestId = null;
	}

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
