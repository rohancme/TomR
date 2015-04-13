package network;

import org.codehaus.jackson.annotate.JsonProperty;

import edu.tomr.protocol.AckMessage;

public class NWResponse {
	
	@JsonProperty private String destIP=null;
	@JsonProperty private String srcIP=null;	
	@JsonProperty private AckMessage ackMsg=null;
	
	public NWResponse(String srcIP,String destIP,AckMessage msg){
		this.srcIP=srcIP;
		this.destIP=destIP;
		this.ackMsg=msg;
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
