package network.requests;

import static network.NetworkConstants.*;
import network.NetworkConstants;
import network.NetworkConstants.Requests;

public abstract class NWRequest {
	
	protected final String request_id;
	protected final String request_type;
	
	protected NWRequest(String req_id,Requests request){
		this.request_id=req_id;
		this.request_type=requestToString(request);
	}
	
	public String getRequestType(){
		return this.request_type;
	}
	
	public String getRequestID(){
		return this.request_id;
	}
}
