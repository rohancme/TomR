package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class AddNodeMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private String ipAddress;
	
	public AddNodeMessage() {}
	
	public AddNodeMessage(String msg, String ipAddress) {
		super();
		this.msg = msg;
		this.ipAddress = ipAddress;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
}
