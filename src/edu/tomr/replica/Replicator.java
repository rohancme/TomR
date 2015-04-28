package edu.tomr.replica;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.replica.ValueUpdator.Operation;
import edu.tomr.replica.network.ReplicaNetworkModule;
import edu.tomr.replica.protocol.ReplicaTransferMessage;

public class Replicator {

	private Set<ValueUpdator> operationSetSyncer;

	private ReplicaNetworkModule networkModule;

	private IMapOperation operation;

	private Replicator(IMapOperation operation) {
		this.operationSetSyncer = new HashSet<ValueUpdator>();
		this.operation = operation;
	}

	public Replicator(IMapOperation operation, boolean isServer) {
		this(operation);
		this.networkModule = new ReplicaNetworkModule(this, isServer, null);
	}

	public Replicator(IMapOperation operation, boolean isServer, String primaryNode) {
		this(operation);
		this.networkModule = new ReplicaNetworkModule(this, isServer, primaryNode);
	}

	public void startReplicating() {
		this.networkModule.startModule();
	}

	public void saveOperation(Operation oper, String key) {
		this.operationSetSyncer.add(new ValueUpdator(oper, key));
	}

	public void updateOperation(Operation oper, String key) {

		ValueUpdator temp = new ValueUpdator(key);
		for(ValueUpdator x: this.operationSetSyncer) {
			if(x.equals(temp)) {
				this.operationSetSyncer.remove(new ValueUpdator(key));
				this.operationSetSyncer.add(new ValueUpdator(oper, key));
				break;
			}
		}
	}

	public ReplicaTransferMessage getSyncMessage() {

		Map<String, byte[]> map = new HashMap<String, byte[]>();
		byte[] value = null;
		for(ValueUpdator v: this.operationSetSyncer) {
			if(v.getOperation() != Operation.DELETE)
				value = operation.get(v.getKey());

			map.put(v.getKey(), value);
			value = null;
		}
		this.operationSetSyncer = new HashSet<ValueUpdator>();

		//this.networkModule.sendUpdates(new ReplicaTransferMessage(map));
		return new ReplicaTransferMessage(map);
	}

	public void syncStorage(Map<String, byte[]> updates) {

		for(Entry<String, byte[]> entry: updates.entrySet()) {

			if(entry.getValue().length != 0)
				this.operation.put(entry.getKey(), entry.getValue());
			else
				this.operation.delete(entry.getKey());
		}
	}

	public boolean contains(String key) {
		return this.operationSetSyncer.contains(new ValueUpdator(key));
	}
}
