package edu.tomr.client;

import org.codehaus.jackson.annotate.JsonProperty;

public class KeyValuePair {
	@JsonProperty("key")private final String key;
	@JsonProperty("value")private final byte[] value;

	private KeyValuePair(){
		this.key=null;
		this.value=null;
	}
	
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
