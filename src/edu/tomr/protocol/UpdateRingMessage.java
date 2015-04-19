package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateRingMessage extends Message {

	@JsonProperty private final String newNode;

	public UpdateRingMessage() {
		super();
		this.newNode = null;
	}

	public UpdateRingMessage(String newNode) {
		super();
		this.newNode = newNode;
	}

	public String getNewNode() {
		return newNode;
	}

}
