package edu.tomr.protocol;

import java.util.ArrayList;
import java.util.List;

public class StartupMessage extends Message {

	private String msg;
	private boolean connectFirst;
	private ArrayList<String> neighborList;
	
	public StartupMessage(String msg,List<String> neighbors){
		this.msg=msg;
		connectFirst=false;
		//in case the neighbor list is a list of a different kind
		neighborList=new ArrayList<String>();
		neighborList.addAll(neighbors);
	}
	
	public StartupMessage(String msg,List<String> neighbors,boolean connectFirst){
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

}
