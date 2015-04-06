package network.requests;

import static network.NetworkConstants.*;
import edu.tomr.protocol.CloseMessage;
import edu.tomr.protocol.Message;
import network.NetworkConstants;
import network.NetworkConstants.Requests;

public abstract class NWRequest {
	
	protected final String request_id;
	private final String request_type;
	protected final Message payload;
	
	protected NWRequest(String req_id,Requests request,Message msg){
		this.request_id=req_id;
		this.request_type=requestToString(request);
		this.payload=msg;
	}
	

	public String getRequestType(){
		return this.request_type;
	}
	
	public String getRequestID(){
		return this.request_id;
	}
}
