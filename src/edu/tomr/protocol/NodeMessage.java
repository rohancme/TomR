package edu.tomr.protocol;

public class NodeMessage extends DBMessage {

	private final String originatorIpAddress;

	public NodeMessage(DBMessage message, String originatorIpAddress){
		super(message.getRequestType(), message.getPayload(), message.getRequestId());
		this.originatorIpAddress = originatorIpAddress;
	}
	
	/*public NodeMessage(ClientRequestType requestType, ClientRequestPayload payload,
			String reqestId, String sourceIpAddress) {
		
		super(requestType, payload, reqestId);
		this.originatorIpAddress = sourceIpAddress;
	}*/

	public String getOriginatorIpAddress() {
		return originatorIpAddress;
	}

	
	
}
