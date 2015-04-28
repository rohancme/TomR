package edu.tomr.replica.handler;

import edu.tomr.replica.Replicator;
import edu.tomr.utils.Constants;

public class ReplicatorHandler implements Runnable {

	private Replicator replica;

	public ReplicatorHandler(Replicator replica) {
		this.replica = replica;
	}

	@Override
	public void run() {

		Constants.globalLog.debug("Started replicating thread");
		this.replica.startReplicating();
	}

}
