package network.responses.outgoing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import network.outgoing.NeighborConnection;
import network.responses.NWResponse;
//a thread of this class is created by the NodeResponseModule in order to send outgoing responses in the background
public class OutgoingResponseServicer implements Runnable{
	
	private ConcurrentLinkedQueue<NWResponse> responseQueue=null;
	private ArrayList<NeighborConnection> outgoingNeighborConnections=null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!responseQueue.isEmpty()){
				sendResponse(responseQueue.poll());
			}
		}
	}
	
	private void sendResponse(NWResponse response){
		//this will obviously change when a better overlay network is designed
		for(NeighborConnection nc:outgoingNeighborConnections){
			nc.send_response(response);
		}
	}
	
	public OutgoingResponseServicer(ConcurrentLinkedQueue<NWResponse> queue,List<NeighborConnection>outgoingNeighborConnections){
		this.responseQueue=queue;
		this.outgoingNeighborConnections=new ArrayList<NeighborConnection>();
		this.outgoingNeighborConnections.addAll(outgoingNeighborConnections);
	}

}
