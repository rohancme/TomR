package edu.tomr.replica.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.replica.network.ReplicaClientModule;
import edu.tomr.replica.protocol.AbsReplicaMessage;
import edu.tomr.replica.protocol.ReplicaStartupMessage;
import edu.tomr.utils.Constants;

public class LBMessageHandler implements Runnable {

	private int port;
	private ReplicaClientModule clientNetworkModule;

	public LBMessageHandler(int port, ReplicaClientModule module) {
		this.port = port;
		this.clientNetworkModule = module;
	}

	@Override
	public void run() {

		/*ObjectMapper mapper = new ObjectMapper();

		Scanner scanner;
		try {
			ServerSocket server = new ServerSocket(this.port);
			while(true) {
				Socket socket = server.accept();

				scanner = new Scanner(socket.getInputStream());
				if(scanner.hasNextLine()){
					AbsReplicaMessage message = readTreeForMessage(mapper, scanner.nextLine());

					if(message instanceof ReplicaStartupMessage) {
						processReplicaStartupMessage((ReplicaStartupMessage)message);
					} else if(message instanceof ReplicaChangeMessage) {
						break;
					}
				}

				scanner.close();
			}
			//server.close();
		} catch (IOException e) {

			Constants.globalLog.debug("IOException while adding new node");
			e.printStackTrace();
		}*/
	}

	/*private AbsReplicaMessage readTreeForMessage(ObjectMapper mapper, String json) throws JsonProcessingException, IOException {

		JsonNode root = mapper.readTree(json);
		JsonNode msgType = root.get("type");

		//Class c = ReplicaMessageFactory.getMessageType(msgType.toString());

		if(msgType.toString().equalsIgnoreCase("STARTUP")){
			ReplicaStartupMessage msg = mapper.readValue(root.get("message"), ReplicaStartupMessage.class);
			return msg;
		}
		return null;
	}

	private void processReplicaStartupMessage(ReplicaStartupMessage msg){
		this.clientNetworkModule.processStartupRequest(msg);
	}*/

}
