package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class ClientServiceMessage extends Message{

	@JsonProperty private ClientServiceRequestPayload servicePayload;
	
	
	public ClientServiceMessage(){
		//Default Constructor for Jackson
	}
	
	public ClientServiceMessage(ClientServiceRequestPayload servicePayload){
		this.servicePayload = servicePayload;		
	}
	
	public ClientServiceRequestPayload getServicePayload(){
		return this.servicePayload;
	}
	
	public void ServicePayload(ClientServiceRequestPayload servicePayload){
		this.servicePayload = servicePayload;
	}
}
