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
			this.startListening();
		} catch (IOException e) {
			System.out.println("Error while trying to connect with the client ");
			e.printStackTrace();
		}
	}
	
	private void startListening(){
		while(true){
			try {
				(new Thread(new ClientConnectionHandler(LBSocket.accept()))).start();
			} catch (IOException e) {
				System.out.println("Error in the accept block ");
				e.printStackTrace();
			}
		}
	}

}
