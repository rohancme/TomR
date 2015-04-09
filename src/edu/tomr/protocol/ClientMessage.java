package edu.tomr.protocol;

public class ClientMessage extends Message {
	
	private final ClientRequestType requestType;
	private String destinationNodeIpAddress;
	private final ClientRequestPayload payload;
	
	public ClientMessage(ClientRequestType requestType, String ipAddress,
			ClientRequestPayload payload) {
		
		this.requestType = requestType;
		this.destinationNodeIpAddress = ipAddress;
		this.payload = payload;
	}
	
	public ClientMessage() {
		
		this.requestType = null;
		this.destinationNodeIpAddress = null;
		this.payload = null;
	}

	public ClientRequestPayload getPayload() {
		return payload;
	}

	public String getDestinationNode() {
		return destinationNodeIpAddress;
	}

	public void setDestinationNode(String destinationNodeIpAddress) {
		this.destinationNodeIpAddress = destinationNodeIpAddress;
	}

	public ClientRequestType getRequestType() {
		return requestType;
	}

}
