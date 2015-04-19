package edu.tomr.protocol;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import edu.tomr.client.KeyValuePair;

public class RedistributionMessage extends Message {

	@JsonProperty private List<KeyValuePair> keys;

	@SuppressWarnings("unused")
	private RedistributionMessage() {

	}

	public RedistributionMessage(List<KeyValuePair> keys) {
		super();
		this.keys = keys;
	}

}
