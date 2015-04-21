package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateRingMessage extends Message {

	@JsonProperty private final String newNode;
	@JsonProperty private final boolean add;

	public UpdateRingMessage() {
		super();
		this.newNode = null;
		this.add = false;
	}

	public UpdateRingMessage(String newNode, boolean add) {
		super();
		this.newNode = newNode;
		this.add = add;
	}

	public String getNewNode() {
		return newNode;
	}

	public boolean isAdd() {
		return add;
	}

}
