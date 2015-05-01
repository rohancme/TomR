package edu.tomr.client;

import network.Connection;
import network.requests.NWRequest;
import network.responses.NWResponse;
import edu.tomr.protocol.ClientInfo;
import edu.tomr.protocol.ClientRequestPayload;
import edu.tomr.protocol.ClientRequestType;
import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.utils.Constants;

public class ClientRunner implements Runnable {
	
	private String serverIP;
	private int lbport;
	private int servicerPort;
	private String message;
	private ClientRequestType requestType=null;
	private int id;
	private String selfIP;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ClientServiceMessage serviceMessage = getServiceMessage();
		Connection nodeConnection=new Connection(serviceMessage.getServiceIPAddress(),servicerPort);
		DBMessage query=new DBMessage(requestType, new ClientRequestPayload("File-"+id, message.getBytes()), new ClientInfo(selfIP), serviceMessage.getPayloadID());
		NWRequest request=new NWRequest(serviceMessage.getPayloadID(),query);
		nodeConnection.send_request(request);
		//this is block wait method
		NWResponse response=nodeConnection.getnextResponse();
		Constants.globalLog.debug(response.getAckMsg().toString());
	}
	
	public ClientRunner(String serverIP,int lbport,int servicerPort,String message,ClientRequestType requestType,int id,String selfIP){
		this.serverIP=serverIP;
		this.lbport=lbport;
		this.servicerPort=servicerPort;
		this.message=message;
		this.requestType=requestType;
		this.id=id;
		this.selfIP=selfIP;
	}
	
	private ClientServiceMessage getServiceMessage() {
		Connection lbConnection=new Connection(serverIP,lbport);
		NWResponse response=lbConnection.getnextResponse();
		return response.getClientServiceMsg();
	}

}
