package edu.tomr.client;

public class KeyValuePair {
	private final String key;
	private final byte[] value;

	public KeyValuePair(String key, byte[] value){
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public byte[] getValue() {
		return value;
	}

}
