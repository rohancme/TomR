package edu.tomr.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import network.Connection;
import network.NetworkConstants;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;
import network.responses.NWResponse;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.protocol.BreakFormationMessage;
import edu.tomr.protocol.UpdateConnMessage;
import edu.tomr.protocol.UpdateNodeAckMessage;
import edu.tomr.protocol.UpdateRingMessage;
import edu.tomr.utils.ConfigParams;
import edu.tomr.utils.Constants;

public class NodeMessageHandler implements Runnable {

	private Socket socket;

	public NodeMessageHandler(Socket socket) {
		this.socket = socket;
	} 
	
	@Override
	public void run() {
		
		NetworkUtilities utils = null;
		ObjectMapper mapper = new ObjectMapper();
		NWResponse response = null;
		UpdateNodeAckMessage message = null;
		Scanner scanner;
		try {
			utils = new NetworkUtilities();
			scanner = new Scanner(socket.getInputStream());
			if(scanner.hasNextLine()){
				response = mapper.readValue(scanner.nextLine(), NWResponse.class);
			}
			scanner.close();
			//message = get message from NWResponse Class
			
			if(message.isAdd()) {
				
				sendUpdateRingMessage(ConfigParams.getIpAddresses(), message.getUpdateNodeAddress(), message.isAdd());
				
			} else {
				
				List<String> originalNodes = ConfigParams.getIpAddresses();
				
				String nodeToRemove = message.getUpdateNodeAddress();
				String predec = ConfigParams.getPredecessorNode(nodeToRemove);
				originalNodes.remove(nodeToRemove);
				
				String newNodeSucessor = ConfigParams.getSuccesorNode(message.getUpdateNodeAddress());
				NWRequest breakFormRequest = utils.getNewBreakFormRequest(new 
						BreakFormationMessage("Break_Form", newNodeSucessor, newNodeSucessor));
				Connection temp_connection=new Connection(predec , NetworkConstants.C_SERVER_LISTEN_PORT);
				temp_connection.send_request(breakFormRequest);
				Constants.globalLog.debug("AddMessageHandler: Break from request to node: "+predec);
				
				ConsistentHashing.updateCircle(originalNodes);
				ConfigParams.removeIpAddress(nodeToRemove);
						
				sendUpdateRingMessage(ConfigParams.getIpAddresses(), nodeToRemove, message.isAdd());
			}
			
		} catch (IOException e) {

			Constants.globalLog.debug("IOException while adding new node");
			e.printStackTrace();
		} catch (NetworkException e) {

			Constants.globalLog.debug("NwException while adding new node");
			e.printStackTrace();
		}
	}

	private void sendUpdateRingMessage(List<String> originalNodes, String newNode, boolean add){

		NetworkUtilities utils=null;

		try {
			utils=new NetworkUtilities();

			for(String ipAddress: originalNodes){

				UpdateRingMessage msg = new UpdateRingMessage(newNode, add);
				NWRequest updateRingRequest = utils.getNewUpdateRingRequest(msg);

				Connection temp_connection=new Connection(ipAddress, NetworkConstants.C_SERVER_LISTEN_PORT);
				temp_connection.send_request(updateRingRequest);
				Constants.globalLog.debug("AddMessageHandler: Sending update ring request to node: "+ipAddress);
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		Constants.globalLog.debug("AddMessageHandler: Sent update ring requests to nodes");

	}
}
