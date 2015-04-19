package network;

import java.io.IOException;
import java.net.Socket;

public class NonPersistentIncomingConnectionHandler extends IncomingConnectionHandler{

	protected NonPersistentIncomingConnectionHandler(int incoming_port) {
		super(incoming_port);
	}
	
	protected Socket getNextSocket(){
		Socket socket=null;
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}
	
	
}
