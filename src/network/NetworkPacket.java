package network;

import java.io.Serializable;

import network.requests.NWRequest;

public class NetworkPacket<T>{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public T request;
	public final String type;
	
	NetworkPacket(T request){
		
		this.type=((NWRequest)request).getRequestType();
		this.request=request;
	}

}
