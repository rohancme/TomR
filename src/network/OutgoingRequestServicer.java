package network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import network.requests.NWRequest;
//a thread for this is spawned by the NodeNeighborModule in order to send outgoing requests in the background
public class OutgoingRequestServicer implements Runnable {

	private ConcurrentLinkedQueue<NWRequest> requestQueue=null;
	private ArrayList<NeighborConnection> outgoingNeighborConnections=null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!requestQueue.isEmpty()){
				sendRequest(requestQueue.poll());
			}
		}
	}
	
	private void sendRequest(NWRequest request){
		//this will obviously change when a better overlay network is designed
		for(NeighborConnection nc:outgoingNeighborConnections){
			nc.send_request(request);
		}
	}
	
	public OutgoingRequestServicer(ConcurrentLinkedQueue<NWRequest> queue,List<NeighborConnection>outgoingNeighborConnections){
		this.requestQueue=queue;
		this.outgoingNeighborConnections=new ArrayList<NeighborConnection>();
		this.outgoingNeighborConnections.addAll(outgoingNeighborConnections);
	}

}
