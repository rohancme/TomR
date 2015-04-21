package edu.tomr.protocol;

import org.codehaus.jackson.annotate.JsonProperty;

public class InitRedistributionMessage extends Message {

	@JsonProperty private final String initRedisNode;

	public InitRedistributionMessage() {
		this.initRedisNode = null;
	}
	
	public InitRedistributionMessage(String initRedisNode) {
		this.initRedisNode = initRedisNode;
	}
	
	public String getInitRedisNode() {
		return initRedisNode;
	}
	
	
}
