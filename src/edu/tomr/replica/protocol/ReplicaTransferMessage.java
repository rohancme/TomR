package edu.tomr.replica.protocol;

import java.util.Map;

public class ReplicaTransferMessage extends AbsReplicaMessage {

	private final Map<String, byte[]> changes;

	public ReplicaTransferMessage(Map<String, byte[]> changes) {
		super();
		this.changes = changes;
	}

	public Map<String, byte[]> getChanges() {
		return changes;
	}

}
