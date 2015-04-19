package network;

import java.io.IOException;
import java.net.ServerSocket;

import edu.tomr.loadbalancer.ClientConnectionHandler;

public class ServerThread implements Runnable {
	private ServerSocket LBSocket = null;
	
	public ServerThread(ServerSocket newSocket){
		this.LBSocket = newSocket;
	}

	@Override
	public void run() {
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
