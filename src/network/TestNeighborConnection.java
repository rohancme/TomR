package network;

public class TestNeighborConnection {

	public static void main(String[] args) {
		
		
		NeighborConnection nc=new NeighborConnection("192.168.1.138",5555);
		
		NW_Request req=new NW_Request("Testing123!");
		
		nc.send_request(req);

	}

}
