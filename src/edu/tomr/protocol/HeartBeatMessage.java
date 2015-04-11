package edu.tomr.protocol;

import java.util.Calendar;
import java.util.Date;

import edu.tomr.network.heartbeat.base.ConnectionAddress;

public class HeartBeatMessage extends Message {

	private ConnectionAddress sender;
	private Date timeStamp;
	
	public ConnectionAddress getSender() {
		return sender;
	}
	public void setSender(ConnectionAddress sender) {
		this.sender = sender;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public HeartBeatMessage(){}
	
	public HeartBeatMessage(ConnectionAddress sender) {
		
		this.sender = sender;
		this.timeStamp = Calendar.getInstance().getTime();
	}
	
	@Override
	public String toString() {
		
		return new StringBuilder().append(sender.getIpAddress()).append(':').append(sender.getPortNumber()).toString();
	}
	
	
}
