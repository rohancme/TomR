package network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import network.requests.NWRequest;

public class NodeNeighborModule {
	
	//this might need to be a copy-on-write later when the neighborConnections become dynamic
	private ArrayList<NeighborConnection> outgoingNeighborConnections=null;
	private ConcurrentLinkedQueue<NWRequest> outgoingRequestQueue=null;
	
	public NodeNeighborModule(List<String> neighborList,int neighborServerPort){
		
		outgoingNeighborConnections=getConnectionList(neighborList,neighborServerPort);
		initializeQueue();
	}
	
	public void startServicingRequests(){
		Thread servicer=new Thread(new OutgoingRequestServicer(outgoingRequestQueue,outgoingNeighborConnections));
		servicer.start();
	}
	
	private ArrayList<NeighborConnection> getConnectionList(List<String> neighborList,int neighborServerPort) {	
		
		ArrayList<NeighborConnection> neighborCons=new ArrayList<NeighborConnection>();
		
		for(String IP:neighborList){
			NeighborConnection connection=new NeighborConnection(IP, neighborServerPort);
			neighborCons.add(connection);
		}	
		return neighborCons;		
	}
	
	public void insertOutgoingRequest(NWRequest request){
		outgoingRequestQueue.add(request);
	}	
	
	private void initializeQueue(){
		this.outgoingRequestQueue=new ConcurrentLinkedQueue<NWRequest>();
	}

}
