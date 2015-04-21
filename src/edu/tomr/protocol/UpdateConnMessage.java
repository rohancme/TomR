package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateConnMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private String newNodeIpAddress;
	@JsonProperty private boolean add;
		
	public UpdateConnMessage() {}
	
	public UpdateConnMessage(String msg, String newNodeIpAddress, boolean isAdd) {
		super();
		this.msg = msg;
		this.newNodeIpAddress = newNodeIpAddress;
		this.add = isAdd;
	}
	
	public UpdateConnMessage(String msg, String newNodeIpAddress) {
		super();
		this.msg = msg;
		this.newNodeIpAddress = newNodeIpAddress;
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

	public boolean isAdd() {
		return add;
	}
	
}
