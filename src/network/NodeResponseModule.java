package network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeResponseModule {
		
		//this might need to be a copy-on-write later when the neighborConnections become dynamic
		private ArrayList<NeighborConnection> outgoingNeighborConnections=null;
		private ConcurrentLinkedQueue<NWResponse> outgoingResponseQueue=null;
		
		public NodeResponseModule(List<String> neighborList,int neighborResponseServerPort){
			
			this.outgoingNeighborConnections=getConnectionList(neighborList,neighborResponseServerPort);
			initializeQueue();
		}
		
		public void startServicingResponses(){
			Thread servicer=new Thread(new OutgoingResponseServicer(outgoingResponseQueue,outgoingNeighborConnections));
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
		
		public void insertOutgoingNWResponse(NWResponse response){
			outgoingResponseQueue.add(response);
		}	
		
		private void initializeQueue(){
			this.outgoingResponseQueue=new ConcurrentLinkedQueue<NWResponse>();
		}


}
