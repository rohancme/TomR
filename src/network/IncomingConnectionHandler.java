package network;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class IncomingConnectionHandler {

	protected ServerSocket serverSocket;
	
	protected IncomingConnectionHandler(int incoming_port){
		try {
			serverSocket=new ServerSocket(incoming_port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
