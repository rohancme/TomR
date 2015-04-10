package edu.tomr.protocol;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StartupMessage extends Message {

	@JsonProperty private String msg;
	@JsonProperty private boolean connectFirst;
	@JsonProperty private ArrayList<String> neighborList;
	@JsonProperty private List<String> nodeList;
	
	public StartupMessage(){
	
	}
	
	private StartupMessage(String msg, List<String> nodeList){
		this.msg = msg;
		this.nodeList = nodeList;
	}
	
	public StartupMessage(String msg, List<String> neighbors, List<String> nodeList){
		
		this(msg, nodeList);
		//in case the neighbor list is a list of a different kind
		neighborList=new ArrayList<String>();
		neighborList.addAll(neighbors);
		
	}
	
	public StartupMessage(String msg,List<String> neighbors, List<String> nodeList, boolean connectFirst){
		
		this(msg, nodeList);
		this.connectFirst=connectFirst;
		this.msg=msg;
		//in case the neighbor list is a list of a different kind
		neighborList=new ArrayList<String>();
		neighborList.addAll(neighbors);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isConnectFirst() {
		return connectFirst;
	}

	public void setConnectFirst(boolean connectFirst) {
		this.connectFirst = connectFirst;
	}

	public ArrayList<String> getNeighborList() {
		return neighborList;
	}

	public void setNeighborList(ArrayList<String> neighborList) {
		this.neighborList = neighborList;
	}

	public List<String> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<String> nodeList) {
		this.nodeList = nodeList;
	}

}
