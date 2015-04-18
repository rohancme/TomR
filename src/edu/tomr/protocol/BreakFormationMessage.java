package edu.tomr.protocol;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class BreakFormationMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private ArrayList<String> newNeighborList;

	public BreakFormationMessage() {}

	public BreakFormationMessage(String msg, ArrayList<String> newNeighborList) {

		this.msg = msg;
		this.newNeighborList = newNeighborList;
	}

	public BreakFormationMessage(String msg, String newNeighbor) {

		this.msg = msg;
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(newNeighbor);
		this.newNeighborList = temp;
	}
}
