package edu.tomr.client;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Connection;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;
import network.responses.NWResponse;
import edu.tomr.protocol.ClientInfo;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.ClientRequestType;
import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.utils.ConfigParams;
import edu.tomr.utils.Constants;

public class InitializeClient {



	static{
		//Get the load balancer IP address
		ConfigParams.loadProperties();
	}

	public static String serverIP;
	private static int lbPort=6000;
	private static int servicerNodePort=5003;

	private static ClientServiceMessage getServiceMessage() {
		serverIP = ConfigParams.getProperty("LB_IP");
		//serverIP = "192.168.1.138";
		//Connect to Load balancer and get servicerIP
		Connection lbConnection=new Connection(serverIP,lbPort);
		NWResponse response=lbConnection.getnextResponse();


		return response.getClientServiceMsg();
	}

	private static void generateRequests(ClientRequestType requestType, int requestLength, int numOfRequests ){
		NetworkUtilities utils = null;
		try {
			utils = new NetworkUtilities();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String randomString = generateString(requestLength);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(int i=1; i<=numOfRequests; i++){
			//ClientServiceMessage serviceMessage = getServiceMessage();
			//Connection nodeConnection=new Connection(serviceMessage.getServiceIPAddress(),servicerNodePort);
			
			//DBMessage query=new DBMessage(requestType, new ClientRequestPayload("File-"+i, randomString.getBytes()), new ClientInfo(utils.getSelfIP()), serviceMessage.getPayloadID());
			//NWRequest request=new NWRequest(serviceMessage.getPayloadID(),query);
			//nodeConnection.send_request(request);
			//this is block wait method
			//NWResponse response=nodeConnection.getnextResponse();
			 Runnable worker = new ClientRunner(serverIP,lbPort,servicerNodePort,randomString,requestType,i,utils.getSelfIP());
			 executor.execute(worker);	
		}
		 executor.shutdown();
	     while (!executor.isTerminated());
	     Constants.globalLog.debug("All client threads finished executing");
	}	

	private static String generateString(int requestLength) {
		final char[] charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
		char [] randomString = new char[requestLength];
		Random rand = new Random();
		for(int i=0;i<randomString.length; i++){
			int valueAt = rand.nextInt(charSet.length);
			randomString[i] = charSet[valueAt];
		}
		String retString = new String(randomString);
		return retString;
	}

	public static void main(String[] args) {
		serverIP = ConfigParams.getProperty("LB_IP");
		long startTime = System.nanoTime();
		//generateRequests(ClientRequestType.ADD, 1000, 2000);
		generateRequests(ClientRequestType.GET, 50, 2000);
		long endTime = System.nanoTime();
		System.out.println("Took "+((endTime - startTime)/1000000) + " ms"); 
	}
}
