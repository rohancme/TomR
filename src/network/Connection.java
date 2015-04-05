package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Connection {
	
	protected Socket socket;

	public Connection(String IP_Address, int port_num) {
		try {
			socket =new Socket(IP_Address,port_num);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host:"+IP_Address);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Some kind of IO Exception at Host:"+IP_Address);
			e.printStackTrace();
		}
	}

}
