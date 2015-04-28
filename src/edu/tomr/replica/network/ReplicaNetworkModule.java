package edu.tomr.replica.network;

import network.NetworkConstants;
import edu.tomr.replica.Replicator;
import edu.tomr.replica.protocol.ReplicaTransferMessage;
import edu.tomr.utils.Constants;

public class ReplicaNetworkModule {

	private ReplicaClientModule client;

	private ReplicaServerModule server;

	private boolean isServer;

	private Replicator parent;

	private String primaryNode;

	public ReplicaNetworkModule(Replicator parent, boolean isServer, String primaryNode) {
		this.isServer = isServer;
		this.parent = parent;
		this.primaryNode = primaryNode;

	}

	public void startModule() {

		Constants.globalLog.debug("TRUE: primary replica || FALSE: client replica");
		Constants.globalLog.debug("Started replication module of "+this.isServer);
		if(this.isServer) {
			initServerModule();
		} else {
			initClientModule(this.primaryNode);
		}
	}

	private void initClientModule(String primaryNode) {

		Constants.globalLog.debug("initialzing client replica module");
		this.client.processStartupRequest(primaryNode);
	}

	private void initServerModule() {

		Constants.globalLog.debug("Initializing server replica module");
		this.server = new ReplicaServerModule(NetworkConstants.C_REPLICA_LISTEN_PORT);
		this.server.acceptIncomingConnections();
		while(true) {
			try {
				Thread.sleep(5000);
				ReplicaTransferMessage msg = this.parent.getSyncMessage();
				if(! msg.getChanges().isEmpty()) {
					this.sendUpdates(msg);
					Constants.globalLog.debug("Sent sync message to client replicas");
				} else {
					Constants.globalLog.debug("No changes to sync");
				}
			} catch (InterruptedException e) {

				Constants.globalLog.equals("Exception while sending sync messages");
				e.printStackTrace();
			}

		}
	}

	public void sendUpdates(ReplicaTransferMessage message) {

		this.server.sendUpdaterMessages(message);
	}

	public void receiveUpdates(ReplicaTransferMessage message) {

		this.parent.syncStorage(message.getChanges());
	}
}
