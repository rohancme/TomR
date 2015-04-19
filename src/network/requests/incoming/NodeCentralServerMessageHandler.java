package network.requests.incoming;

import java.net.Socket;

import network.incoming.nonpersistent.NonPersistentIncomingConnectionHandler;
import edu.tomr.node.base.Node;
//handler for incoming central server requests
public class NodeCentralServerMessageHandler extends NonPersistentIncomingConnectionHandler implements Runnable{
	
	Node currentNode=null;

	public NodeCentralServerMessageHandler(int incoming_port,Node node) {
		super(incoming_port);
		this.currentNode=node;
	}
	
	@Override
	public void run() {
		while(true){
			Socket clientSocket=super.getNextSocket();
			Thread t=new Thread();
			t.start();
		}
	}

}

