package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class ClientServiceMessage extends Message{

	@JsonProperty private String serviceIPAddress=null;
	@JsonProperty private String payloadID=null;
	
	public ClientServiceMessage(){
		//Default Constructor for Jackson
	}
	
	public ClientServiceMessage(String IPAddress,String ID){
		this.serviceIPAddress = IPAddress;		
		this.payloadID=ID;
	}

	public String getPayloadID() {
		return payloadID;
	}

	public String getServiceIPAddress() {
		return serviceIPAddress;
	}
}
