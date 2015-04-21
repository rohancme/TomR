package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateConnMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private String newNodeIpAddress;
	@JsonProperty private String waitForConnIpAddress;
	
	public UpdateConnMessage() {}
	
	public UpdateConnMessage(String msg, String ipAddress, String waitForConnIpAddress) {
		super();
		this.msg = msg;
		this.newNodeIpAddress = ipAddress;
		this.waitForConnIpAddress = waitForConnIpAddress; 
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getNewNodeIpAddress() {
		return newNodeIpAddress;
	}

	public void setNewNodeIpAddress(String newNodeIpAddress) {
		this.newNodeIpAddress = newNodeIpAddress;
	}

	public String getWaitForConnIpAddress() {
		return waitForConnIpAddress;
	}

	public void setWaitForConnIpAddress(String waitForConnIpAddress) {
		this.waitForConnIpAddress = waitForConnIpAddress;
	}
	
	
}
