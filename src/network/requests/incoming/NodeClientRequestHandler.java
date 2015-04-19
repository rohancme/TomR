package network.requests.incoming;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;








import network.incoming.nonpersistent.NonPersistentIncomingConnectionHandler;
import edu.tomr.node.base.Node;
//handler for incoming client requests
public class NodeClientRequestHandler extends NonPersistentIncomingConnectionHandler implements Runnable{
	
	Node currentNode=null;
	ConcurrentHashMap<String,Socket> clientConnectionList=null;

	public NodeClientRequestHandler(int incoming_port,Node node,ConcurrentHashMap<String,Socket> clientConnectionList) {
		super(incoming_port);
		this.currentNode=node;
		this.clientConnectionList=clientConnectionList;
	}
	
	@Override
	public void run() {
		while(true){
			Socket clientSocket=super.getNextSocket();
			Thread t=new Thread(new NodeClientRequestServicer(clientSocket,currentNode,clientConnectionList));
			t.start();
		}
	}

}
