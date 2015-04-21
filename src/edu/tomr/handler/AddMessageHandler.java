package edu.tomr.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import network.Connection;
import network.NetworkConstants;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.protocol.BreakFormationMessage;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.protocol.UpdateConnMessage;
import edu.tomr.protocol.UpdateRingMessage;
import edu.tomr.utils.ConfigParams;
import edu.tomr.utils.Constants;

public class AddMessageHandler implements Runnable {

	private Socket socket;

	public AddMessageHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		NetworkUtilities utils = null;
		ObjectMapper mapper = new ObjectMapper();
		NWRequest request = null;
		UpdateConnMessage message = null;
		Scanner scanner;
		try {
			utils = new NetworkUtilities();
			scanner = new Scanner(socket.getInputStream());
			if(scanner.hasNextLine()){
				request = mapper.readValue(scanner.nextLine(), NWRequest.class);
			}
			scanner.close();
			message = request.getupdateConnMessage();
			
			if(message.isAdd()) {
				//List of addresses before adding the new node
				List<String> originalNodes = ConfigParams.getIpAddresses();
	
				updateConsistentHash(message.getNewNodeIpAddress());
				String predec = ConfigParams.getPredecessorNode(message.getNewNodeIpAddress());
				//Need to send update ring request to all existing nodes
				//originalNodes.remove(predec);
	
				List<String> temp = new ArrayList<String>();
				temp.add(ConfigParams.getSuccesorNode(message.getNewNodeIpAddress()));
	
				NWRequest newStartUpRequest = utils.getNewStartupRequest(new StartupMessage("New_node", temp, ConfigParams.getIpAddresses()));
				Connection temp_connection=new Connection(message.getNewNodeIpAddress() ,NetworkConstants.C_SERVER_LISTEN_PORT);
				temp_connection.send_request(newStartUpRequest);
				Constants.globalLog.debug("AddMessageHandler: Sending startup request to node: "+message.getNewNodeIpAddress());
	
				String newNodeSucessor = ConfigParams.getSuccesorNode(message.getNewNodeIpAddress());
				NWRequest breakFormRequest = utils.getNewBreakFormRequest(new 
						BreakFormationMessage("Break_Form", message.getNewNodeIpAddress(), newNodeSucessor));
				temp_connection=new Connection(predec , NetworkConstants.C_SERVER_LISTEN_PORT);
				temp_connection.send_request(breakFormRequest);
				Constants.globalLog.debug("AddMessageHandler: Break from request to node: "+predec);
	
			} else {
				
				List<String> originalNodes = ConfigParams.getIpAddresses();
				
				String nodeToRemove = message.getNewNodeIpAddress();
				String predec = ConfigParams.getPredecessorNode(nodeToRemove);
				originalNodes.remove(nodeToRemove);
				
				//Send init redistribution to node to be removed
				
				String newNodeSucessor = ConfigParams.getSuccesorNode(message.getNewNodeIpAddress());
				NWRequest breakFormRequest = utils.getNewBreakFormRequest(new 
						BreakFormationMessage("Break_Form", newNodeSucessor, newNodeSucessor));
				Connection temp_connection=new Connection(predec , NetworkConstants.C_SERVER_LISTEN_PORT);
				temp_connection.send_request(breakFormRequest);
				Constants.globalLog.debug("AddMessageHandler: Break from request to node: "+predec);
				
				ConsistentHashing.updateCircle(originalNodes);
				ConfigParams.removeIpAddress(nodeToRemove);
			}

			//TODO: Remove this after ack message is fixed
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			sendUpdateRingMessage(ConfigParams.getIpAddresses(), message.getNewNodeIpAddress(), message.isAdd());
			
		} catch (IOException e) {

			Constants.globalLog.debug("IOException while adding new node");
			e.printStackTrace();
		} catch (NetworkException e) {

			Constants.globalLog.debug("NwException while adding new node");
			e.printStackTrace();
		}
	}

	private void updateConsistentHash(String newAddress) {

		List<String> ips = ConfigParams.getIpAddresses();
		ips.add(newAddress);

		ConsistentHashing.updateCircle(ips);
		ConfigParams.addIpAddress(newAddress);
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
