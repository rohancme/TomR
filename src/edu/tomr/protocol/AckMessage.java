package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class AckMessage {

	@JsonProperty private final boolean status;
	@JsonProperty private final byte[] value;
	@JsonProperty private final String requestIdServiced;

	public AckMessage(boolean status, byte[] value, String reqId) {
		this.status = status;
		this.value = value;
		this.requestIdServiced = reqId;
	}

	public String getRequestIdServiced() {
		return requestIdServiced;
	}

	public boolean isStatus() {
		return status;
	}

	public byte[] getValue() {
		return value;
	}


}
