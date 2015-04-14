package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class AckMessage {

	@JsonProperty private final boolean status;
	@JsonProperty private final byte[] value;
	@JsonProperty private final String requestIdServiced;

	public AckMessage() {
		this.status = false;
		this.value = null;
		this.requestIdServiced = null;
	}

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

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("{AckMessage: status: ");
		builder.append(status);
		builder.append(" value size: ");
		if(value != null)
			builder.append(value.length);
		else
			builder.append(0);
		builder.append(" requestIdServiced: ");
		builder.append(requestIdServiced);
		
		return builder.toString();
	}
	
}
