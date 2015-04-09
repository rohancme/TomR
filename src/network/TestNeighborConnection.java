package network;

import edu.tomr.protocol.StartupMessage;

public class TestNeighborConnection {
	
	//Need new one

	public static void main(String[] args) {
		
		NetworkUtilities myNetworkModule=null;
		
		try {
			myNetworkModule=new NetworkUtilities();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		NeighborConnection nc=new NeighborConnection("192.168.1.138",5555);
		
		//NetworkPacket req=myNetworkModule.getNewStartupRequest(new StartupMessage("GET JIGGY WITH IT"));
		
		//nc.send_request(req);

	}

}
