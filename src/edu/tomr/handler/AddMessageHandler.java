package edu.tomr.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import network.Connection;
import network.NetworkConstants;
import network.NetworkException;
import network.NetworkUtilities;
import network.requests.NWRequest;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.protocol.AddNodeMessage;
import edu.tomr.protocol.BreakFormationMessage;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.protocol.UpdateRingMessage;
import edu.tomr.utils.ConfigParams;

public class AddMessageHandler implements Runnable {

	private Socket socket;

	public AddMessageHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		NetworkUtilities utils = null;
		ObjectMapper mapper = new ObjectMapper();
		AddNodeMessage message = null;
		Scanner scanner;
		try {
			utils = new NetworkUtilities();
			scanner = new Scanner(socket.getInputStream());
			if(scanner.hasNextLine()){
				message = mapper.readValue(scanner.nextLine(), AddNodeMessage.class);
			}
			scanner.close();

			//List of addresses before adding the new node
			List<String> originalNodes = ConfigParams.getIpAddresses();

			updateConsistentHash(message.getIpAddress());
			String predec = ConfigParams.getPredecessorNode(message.getIpAddress());
			originalNodes.remove(predec);

			List<String> temp = new ArrayList<String>();
			temp.add(ConfigParams.getSuccesorNode(message.getIpAddress()));

			NWRequest newStartUpRequest = utils.getNewStartupRequest(new StartupMessage("New_node", temp, ConfigParams.getIpAddresses()));
			Connection temp_connection=new Connection(message.getIpAddress() ,NetworkConstants.C_SERVER_LISTEN_PORT);
			temp_connection.send_request(newStartUpRequest);

			NWRequest breakFormRequest = utils.getNewBreakFormRequest(new BreakFormationMessage("Break_Form", message.getIpAddress()));
			temp_connection=new Connection(predec , NetworkConstants.C_SERVER_LISTEN_PORT);
			temp_connection.send_request(breakFormRequest);

			sendUpdateRingMessage(originalNodes, message.getIpAddress());

		} catch (IOException e) {

			System.out.println("IOException while adding new node");
			e.printStackTrace();
		} catch (NetworkException e) {

			System.out.println("NwException while adding new node");
			e.printStackTrace();
		}
	}

	private void updateConsistentHash(String newAddress) {

		List<String> ips = ConfigParams.getIpAddresses();
		ips.add(newAddress);

		ConsistentHashing.updateCircle(ips);
		ConfigParams.addIpAddress(newAddress);
	}

	private void sendUpdateRingMessage(List<String> originalNodes, String newNode){

		NetworkUtilities utils=null;

		try {
			utils=new NetworkUtilities();

			for(String ipAddress: originalNodes){

				UpdateRingMessage msg = new UpdateRingMessage(newNode);
				NWRequest updateRingRequest = utils.getNewUpdateRingRequest(msg);

				Connection temp_connection=new Connection(ipAddress, NetworkConstants.C_SERVER_LISTEN_PORT);

				temp_connection.send_request(updateRingRequest);

			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		System.out.println("Sent update ring requests to nodes");

	}

}
