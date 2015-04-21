package network.responses;

import org.codehaus.jackson.annotate.JsonProperty;

import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.ClientServiceMessage;
import edu.tomr.protocol.UpdateNodeAckMessage;
//a container for Network Responses
public class NWResponse {
	
	public ClientServiceMessage getClientServiceMsg() {
		return clientServiceMsg;
	}

	@JsonProperty private String destIP=null;
	@JsonProperty private String srcIP=null;	
	@JsonProperty private AckMessage ackMsg=null;
	@JsonProperty private ClientServiceMessage clientServiceMsg=null;
	@JsonProperty private boolean resetIncomingResponseMsg=false;
	@JsonProperty private UpdateNodeAckMessage updateNodeAckMsg = null;
	
	public boolean isResetIncomingResponseMsg() {
		return resetIncomingResponseMsg;
	}

	public void setResetIncomingResponseMsg() {
		this.resetIncomingResponseMsg = true;
	}
	
	//only for jackson
	private NWResponse(){
		
	}


	public NWResponse(String srcIP,String destIP,AckMessage msg){
		this.srcIP=srcIP;
		this.destIP=destIP;
		this.ackMsg=msg;
	}
	
	
	//use this only when need to tell a node to break its incoming Response Connection
	public NWResponse(String srcIP,String destIP){
		this.srcIP=srcIP;
		this.destIP=destIP;
		this.ackMsg=null;
	}
	
	//used for client responses
	public NWResponse(AckMessage msg){
		this.ackMsg=msg;
	}
	
	/**
	 * Used for sending acknowledgements to server
	 * @param msg
	 */
	public NWResponse(UpdateNodeAckMessage msg){
		this.updateNodeAckMsg=msg;
	}

	public NWResponse(ClientServiceMessage serviceMessage) {
		this.clientServiceMsg=serviceMessage;
	}

	public String getDestIP() {
		return destIP;
	}

	public String getSrcIP() {
		return srcIP;
	}

	public AckMessage getAckMsg() {
		return ackMsg;
	}

}
