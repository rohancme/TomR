package edu.tomr.protocol;

public class NodeMessage extends DBMessage {

	private final String sourceIpAddress;

	public NodeMessage(ClientRequestType requestType, ClientRequestPayload payload,
			String reqestId, String sourceIpAddress) {
		
		super(requestType, payload, reqestId);
		this.sourceIpAddress = sourceIpAddress;
	}

	public String getSourceIpAddress() {
		return sourceIpAddress;
	}
	
}
