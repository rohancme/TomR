package edu.tomr.client;

import java.util.Scanner;

import edu.tomr.utils.ConfigParams;

public class InitializeClient {
	
	
	
	static{
		//Get the load balancer IP address
		ConfigParams.loadProperties();
	}
	
	public static String serverIP;
	
private static KeyValuePair getKeyValue() {
		System.out.println("Enter the key :");
		Scanner input = new Scanner(System.in);
		String key = input.nextLine();
		System.out.println("Enter the Value for this key :");
		String value = input.nextLine();
		return (new KeyValuePair(key, value));
		
	}

private static String getServiceIAddress() {
	//serverIP = ConfigParams.getProperty(LB_IP);
	serverIP = "";
	//Connect to Load balancer and get servicerIP
	
	return null;
}




	
	public static void main(String[] args) {
		KeyValuePair inputTuple;
		inputTuple = getKeyValue();
		String serviceIPAddress = getServiceIAddress();
		//Send the request to the particular node.
		

	}



	
}
