package edu.tomr.client;

import java.util.Scanner;

import network.Connection;
import network.NWResponse;
import network.NetworkException;
import network.NetworkUtilities;
import network.requests.NWRequest;
import edu.tomr.protocol.ClientInfo;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.ClientRequestType;
import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.utils.ConfigParams;

public class InitializeClient {



	static{
		//Get the load balancer IP address
		ConfigParams.loadProperties();
	}

	public static String serverIP;
	private static int lbPort=6000;
	private static int servicerNodePort=5003;

private static KeyValuePair getKeyValue() {
		System.out.println("Enter the key :");
		Scanner input = new Scanner(System.in);
		String key = input.nextLine();
		System.out.println("Enter the Value for this key :");
		String value = input.nextLine();
		return (new KeyValuePair(key, value));

	}

	private static ClientServiceMessage getServiceMessage() {
		//serverIP = ConfigParams.getProperty(LB_IP);
		serverIP = "192.168.1.138";

		//Connect to Load balancer and get servicerIP
		Connection lbConnection=new Connection(serverIP,lbPort);
		NWResponse response=lbConnection.getnextResponse();


		return response.getClientServiceMsg();
	}



	public static void main(String[] args) {
		KeyValuePair inputTuple=null;

		NetworkUtilities utils = null;
		try {
			utils = new NetworkUtilities();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//inputTuple = getKeyValue();
		ClientServiceMessage serviceMessage = getServiceMessage();
		//Send the request to the particular node.
		Connection nodeConnection=new Connection(serviceMessage.getServiceIPAddress(),servicerNodePort);
		//this is dummy code. will need to be updated once we figure out how we're accepting client queries
		for(int i=1; i<=3; i++){
			System.out.println("Sending request for File-"+ i);
			DBMessage query=new DBMessage(ClientRequestType.ADD, new ClientRequestPayload("File-"+i, new String("File-"+i).getBytes()), new ClientInfo(utils.getSelfIP()), serviceMessage.getPayloadID());
			NWRequest request=new NWRequest(serviceMessage.getPayloadID(),query);
			nodeConnection.send_request(request);
			//this is block wait method
			NWResponse response=nodeConnection.getnextResponse();

			System.out.println(response.getAckMsg().toString());
		}
	}




}
