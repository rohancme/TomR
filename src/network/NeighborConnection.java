package network;
//class to establish a connection with a neighbor that is listening for connections
public class NeighborConnection extends Connection{
	
	public NeighborConnection(String IP_Address, int port_num){
		super(IP_Address,port_num);
	}

}
