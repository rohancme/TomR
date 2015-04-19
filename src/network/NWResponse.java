package network;

import org.codehaus.jackson.annotate.JsonProperty;

import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.ClientServiceMessage;
//a container for Network Responses
public class NWResponse {
	
	public ClientServiceMessage getClientServiceMsg() {
		return clientServiceMsg;
	}

	@JsonProperty private String destIP=null;
	@JsonProperty private String srcIP=null;	
	@JsonProperty private AckMessage ackMsg=null;
	@JsonProperty private ClientServiceMessage clientServiceMsg=null;
	
	public NWResponse(String srcIP,String destIP,AckMessage msg){
		this.srcIP=srcIP;
		this.destIP=destIP;
		this.ackMsg=msg;
	}
	
	//used for client responses
	public NWResponse(AckMessage msg){
		this.ackMsg=msg;
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
