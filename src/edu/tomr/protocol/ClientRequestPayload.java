package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public final class ClientRequestPayload {

	@JsonProperty private final String key;
	@JsonProperty private byte[] value;

	public ClientRequestPayload() {
		this.key = null;
	}

	public ClientRequestPayload(String key, byte[] value) {

		this.key = key;
		this.value = value;
	}

	public ClientRequestPayload(String key) {

		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public String toString() {

		String str = "{ClientRequestPayload:"
				+ "key: "+key+" value size: "+value.length+"}";
		return str;
	}



}
