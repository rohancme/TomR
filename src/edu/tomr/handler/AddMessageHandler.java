package edu.tomr.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import network.Connection;
import network.NetworkConstants;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;

import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.hash.ConsistentHashing;
import edu.tomr.protocol.AddNodeMessage;
import edu.tomr.protocol.BreakFormationMessage;
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
			String predec = ConfigParams.getRandomIpAddress();

			NWRequest breakFormRequest = utils.getNewBreakFormRequest(new BreakFormationMessage("Break_Form", message.getIpAddress()));
			Connection temp_connection=new Connection(predec , NetworkConstants.C_SERVER_LISTEN_PORT);

			temp_connection.send_request(breakFormRequest);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}
