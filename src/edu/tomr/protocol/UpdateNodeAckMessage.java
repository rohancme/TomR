package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateNodeAckMessage extends Message {

	@JsonProperty private final boolean add;
	@JsonProperty private final String updateNodeAddress;

	@SuppressWarnings("unused")
	private UpdateNodeAckMessage(){
		this.add = false;
		this.updateNodeAddress = null;
	}
	
	public UpdateNodeAckMessage(boolean add, String updateNodeAddress) {
		super();
		this.add = add;
		this.updateNodeAddress = updateNodeAddress;
	}

	public boolean isAdd() {
		return add;
	}

	public String getUpdateNodeAddress() {
		return updateNodeAddress;
	}
	
}
