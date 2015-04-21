package network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import network.exception.NetworkException;
import network.requests.NWRequest;
import edu.tomr.protocol.UpdateConnMessage;
import edu.tomr.protocol.BreakFormationMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.NeighborMessage;
import edu.tomr.protocol.RedistributionMessage;
import edu.tomr.protocol.StartupMessage;
import edu.tomr.protocol.UpdateRingMessage;

public class NetworkUtilities {

	private final String IP;
	public RandomIDGenerator randomGen;

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
	    
	    randomGen=new RandomIDGenerator(this.IP);
	}

	public NetworkUtilities(String IP){
		this.IP=IP;
	}

	public String getSelfIP(){
		return this.IP;
	}

	public String generate_req_id(){
		//return (IP+UUID.randomUUID());
		return randomGen.getRandomID();
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

	public NWRequest getNewDBRequest(DBMessage msg,String destIP){
		NWRequest request=new NWRequest(this.generate_req_id(),msg,this.getSelfIP(),destIP);
		return request;
	}

	public NWRequest getNewBreakFormRequest(BreakFormationMessage msg)  {
		NWRequest request = new NWRequest(this.generate_req_id(), msg, this.getSelfIP());
		return request;
	}

	public NWRequest getNewUpdateRingRequest(UpdateRingMessage msg) {
		NWRequest request = new NWRequest(this.generate_req_id(), msg);
		return request;
	}

	public NWRequest getNewRedisRequest(RedistributionMessage msg,String destIP) {
		NWRequest request = new NWRequest(this.generate_req_id(), msg,this.getSelfIP(),destIP);
		return request;
	}
	
	public NWRequest getNewAddNodeRequest(UpdateConnMessage msg) {
		NWRequest request = new NWRequest(this.generate_req_id(), msg);
		return request;
	}
}
