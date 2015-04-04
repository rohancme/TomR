package network;

import java.util.UUID;

import requests.CloseRequest;
import requests.NewClientConnectionRequest;

public class NetworkModule {
	
	String IP;
	
	NetworkModule(String IP){
		this.IP=IP;
	}
	
	String generate_req_id(){
		return (IP+UUID.randomUUID());
	}
	
	NewClientConnectionRequest getNewClientConnectionRequest(){
		
		NewClientConnectionRequest request=new NewClientConnectionRequest(this.generate_req_id());
		
		return request;
	}
	
	CloseRequest getCloseRequest(){
		
		CloseRequest request=new CloseRequest(this.generate_req_id());
		
		return request;
	}

}
