package network;

import network.requests.NWRequest;

public class NetworkPacket<T>{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public T request;
	public final String type;
	
	public NetworkPacket(T request){
		
		this.type=((NWRequest)request).getRequestType();
		this.request=request;
	}

}
