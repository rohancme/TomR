package edu.tomr.replica.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import network.NetworkConstants;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.replica.protocol.ReplicaTransferMessage;

public class ReplicaClientModule {

	private Socket clientSocket;
	private boolean stopListening = true;
	private ReplicaNetworkModule parentModule;

	public ReplicaClientModule(ReplicaNetworkModule parentModule) {
		this.parentModule =  parentModule;
	}

	/*public void initReplicaNetwork() throws IOException {

		LBMessageHandler handler = new LBMessageHandler(NetworkConstants.C_SERVER_LISTEN_PORT, this);
		Thread lbHandlerThread =  new Thread(handler);
		lbHandlerThread.start();
	}*/

	public void processStartupRequest(String primaryNode) {

		try {
			this.clientSocket = new Socket(primaryNode, NetworkConstants.C_REPLICA_LISTEN_PORT);
			this.startAcceptingMessages();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void changeRequest() {

		//change the stopListening flag to true
		//then move ahead
	}

	private void startAcceptingMessages() {

		Scanner scanner = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		while(this.stopListening) {

			try {
				scanner = new Scanner(this.clientSocket.getInputStream());

				while(scanner.hasNextLine()) {
					ReplicaTransferMessage message = mapper.readValue(scanner.nextLine(), ReplicaTransferMessage.class);
					this.parentModule.receiveUpdates(message);
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		scanner.close();
	}

}
