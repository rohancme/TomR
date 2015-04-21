package network.requests.incoming;

import java.io.IOException;
import java.net.ServerSocket;

import edu.tomr.utils.Constants;

public class LBClientServer {

	static int serverPort=6000;
	static ServerSocket LBSocket;
	
	public LBClientServer(){
		try {
			LBSocket = new ServerSocket(serverPort);
			new Thread(new ServerThread(LBSocket)).start();
		} catch (IOException e) {
			Constants.globalLog.debug("Error while trying to connect with the client ");
			e.printStackTrace();
		}
	}
	
}
