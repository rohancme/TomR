package network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import network.requests.NWRequest;
import edu.tomr.protocol.NeighborMessage;
import edu.tomr.protocol.StartupMessage;

public class NetworkUtilities {
	
	String IP;
	
	public NetworkUtilities() throws NetworkException{
		//following code is from:http://stackoverflow.com/a/18945245
		String ipAddress = null;
	    Enumeration<NetworkInterface> net = null;
	    try {
	        net = NetworkInterface.getNetworkInterfaces();
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }

	    while(net.hasMoreElements()){
	        NetworkInterface element = net.nextElement();
	        Enumeration<InetAddress> addresses = element.getInetAddresses();
	        while (addresses.hasMoreElements()){
	            InetAddress ip = addresses.nextElement();
	            if (ip instanceof Inet4Address){
	                if (ip.isSiteLocalAddress()){
	                    ipAddress = ip.getHostAddress();
	                }
	            }
	        }
	    }	    
	    if(ipAddress==null){
	    	throw new NetworkException("Error initializing IP address. Try defining the IP address in the config file.");
	    }
	    else{
	    	this.IP=ipAddress;
	    }
	}
	
	public NetworkUtilities(String IP){
		this.IP=IP;
	}
	
	public String generate_req_id(){
		return (IP+UUID.randomUUID());
	}
	
	
	/*public NetworkPacket<NewClientConnectionRequest> getNewClientConnectionRequest(Message msg){
		NewClientConnectionRequest request=new NewClientConnectionRequest(this.generate_req_id(),msg);
		return new NetworkPacket<NewClientConnectionRequest>(request);
	}*/
	
	public NWRequest getNewNeighborConnectionRequest(NeighborMessage msg){
		NWRequest request=new NWRequest(this.generate_req_id(),msg);
		return request;
	}
	
	/*public NWRequest getNewCloseRequest(Message msg){	
		CloseRequest request=new CloseRequest(this.generate_req_id(),msg);
		return new NetworkPacket<CloseRequest>(request);
	}*/
		
	public NWRequest getNewStartupRequest(StartupMessage msg){	
		NWRequest request=new NWRequest(this.generate_req_id(),msg);
		return request;
	}
	
	/*public NetworkPacket<DBRequest> getNewDBRequest(Message msg){	
		DBRequest request=new DBRequest(this.generate_req_id(),msg);
		return new NetworkPacket<DBRequest>(request);
	}*/

}
