package edu.tomr.protocol;

public class AckMessage {

	private final boolean status;
	private final byte[] value;
	private final String requestIdServiced;
	
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
