package edu.tomr.protocol;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class BreakFormationMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private ArrayList<String> newNeighborList;
	@JsonProperty private String waitForConnIpAddress;

	public BreakFormationMessage() {}

	public BreakFormationMessage(String msg, ArrayList<String> newNeighborList, String waitForConnIpAddress) {

		this.msg = msg;
		this.newNeighborList = newNeighborList;
		this.waitForConnIpAddress = waitForConnIpAddress;
	}

	public BreakFormationMessage(String msg, String newNeighbor, String waitForConnIpAddress) {

		this.msg = msg;
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(newNeighbor);
		this.newNeighborList = temp;
		this.waitForConnIpAddress = waitForConnIpAddress;		
	}

	public ArrayList<String> getNewNeighborList() {
		return newNeighborList;
	}

	public String getWaitForConnIpAddress() {
		return waitForConnIpAddress;
	}
	
}
