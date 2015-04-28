package edu.tomr.replica.protocol;

import java.util.List;

public class ReplicaStartupMessage extends AbsReplicaMessage {

	private String primaryNode;
	private List<String> replicas;

	public ReplicaStartupMessage(String primaryNode, List<String> replicas) {
		super();
		this.primaryNode = primaryNode;
		this.replicas = replicas;
	}

	public String getPrimaryNode() {
		return primaryNode;
	}
	public void setPrimaryNode(String primaryNode) {
		this.primaryNode = primaryNode;
	}
	public List<String> getReplicas() {
		return replicas;
	}
	public void setReplicas(List<String> replicas) {
		this.replicas = replicas;
	}


}
