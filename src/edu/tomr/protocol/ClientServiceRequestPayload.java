package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public  class ClientServiceRequestPayload {

	@JsonProperty private String serviceIPAddress;
	
	public ClientServiceRequestPayload(){
		this.serviceIPAddress = null;
	}
	
	public ClientServiceRequestPayload(String IPAddress){
		this.serviceIPAddress = IPAddress;
	}
	
	public String getServiceIP(){
		return serviceIPAddress;
	}
	
	public void setServiceIP(String IPAddress){
		this.serviceIPAddress = IPAddress;
	}
}
