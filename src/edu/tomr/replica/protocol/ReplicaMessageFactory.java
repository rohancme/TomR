package edu.tomr.replica.protocol;

import java.util.List;

public class ReplicaMessageFactory {

	public static ReplicaStartupMessage getReplicaStartupMessage(String primaryNode, List<String> replicas) {

		return new ReplicaStartupMessage(primaryNode, replicas);
	}

	/*public static ReplicaTransferMessage getReplicaTransferMessage() {

	}

	public static ReplicaChangeMessage getReplicaChangeMessage() {

	}*/

	public static Class getMessageType(String type) {

		if(type.equalsIgnoreCase("STARTUP"))
			return ReplicaStartupMessage.class;
		return null;
	}
}
