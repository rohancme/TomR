package edu.tomr.replica.network;

import java.util.ArrayList;
import java.util.List;

import network.exception.NetworkException;
import network.incoming.IncomingConnectionHandler;
import edu.tomr.replica.protocol.AbsReplicaMessage;
import edu.tomr.utils.ConfigParams;
import edu.tomr.utils.Constants;

public class ReplicaServerModule extends IncomingConnectionHandler {

	List<ReplicaConnectionHandler> replicaConnections;

	public ReplicaServerModule(int incoming_port) {
		super(incoming_port);
		replicaConnections = new ArrayList<ReplicaConnectionHandler>();
		for(int i=0; i< ConfigParams.getIntProperty("REPLICATION_FACTOR"); i++) {

			ReplicaConnectionHandler handler = new ReplicaConnectionHandler(incoming_port);
			replicaConnections.add(handler);
		}
	}

	public void acceptIncomingConnections() {

		for(ReplicaConnectionHandler handler: replicaConnections) {

			try {
				handler.initSocket(super.serverSocket);
			} catch (NetworkException e) {
				Constants.globalLog.error("Error while accpeting incoming replica connection");
				e.printStackTrace();
			}

		}
	}

	public void sendUpdaterMessages(AbsReplicaMessage message) {

		for(ReplicaConnectionHandler handler: replicaConnections) {

			handler.sendMessage(message);
		}
	}

}
