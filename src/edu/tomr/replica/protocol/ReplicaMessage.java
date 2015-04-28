package edu.tomr.replica.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReplicaMessage {

	@JsonProperty private final String type;
	@JsonProperty private final AbsReplicaMessage message;

	public ReplicaMessage(String type, AbsReplicaMessage message) {
		super();
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}
	public AbsReplicaMessage getMessage() {
		return message;
	}

}
