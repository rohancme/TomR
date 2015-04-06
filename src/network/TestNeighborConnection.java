package network;

import edu.tomr.protocol.StartupMessage;
import network.requests.StartupRequest;

public class TestNeighborConnection {

	public static void main(String[] args) {
		
		NetworkModule myNetworkModule=null;
		
		try {
			myNetworkModule=new NetworkModule();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		NeighborConnection nc=new NeighborConnection("192.168.1.138",5555);
		
		NetworkPacket req=myNetworkModule.getNewStartupRequest(new StartupMessage("GET JIGGY WITH IT"));
		
		nc.send_request(req);

	}

}
