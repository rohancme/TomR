package edu.tomr.protocol;

public class CloseMessage extends Message {

	private final String reason;
	
	public CloseMessage(String reason) {
		this.reason=reason;
	}
	
	public CloseMessage(){
		this.reason="generic_close_message";
	}

}
