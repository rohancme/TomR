package network;

import java.io.IOException;
import java.net.ServerSocket;

import edu.tomr.loadbalancer.ClientConnectionHandler;

public class LBClientServer {

	static int serverPort=6000;
	static ServerSocket LBSocket;
	
	public LBClientServer(){
		try {
			LBSocket = new ServerSocket(serverPort);
			System.out.println("Listening on port:"+serverPort);
			new Thread(new ServerThread(LBSocket)).start();
			
		} catch (IOException e) {
			System.out.println("Error while trying to connect with the client ");
			e.printStackTrace();
		}
	}
	
}
