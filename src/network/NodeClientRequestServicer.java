package network;

import java.net.Socket;

import edu.tomr.node.base.Node;
import edu.tomr.protocol.DBMessage;
import network.requests.NWRequest;

public class NodeClientRequestServicer implements Runnable {
	
	Socket mySocket=null;
	Node myNode=null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		NWRequest clientRequest=
	}
	
	public NodeClientRequestServicer(Socket socket,Node node){
		this.mySocket=socket;
		this.myNode=node;
	}
	
	private void handleRequest(NWRequest request){
		
		//currently clients are only sending DBMessage. Don't see this changing
		DBMessage msg=request.getdBMessage();
		myNode.handleRequest(msg);
	}

}
