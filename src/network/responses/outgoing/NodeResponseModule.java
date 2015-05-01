package network.responses.outgoing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import network.outgoing.NeighborConnection;
import network.responses.ClientResponseWrapper;
import network.responses.NWResponse;
//handles all outgoing response messages from this node
public class NodeResponseModule {
		
		//this might need to be a copy-on-write later when the neighborConnections become dynamic
		private ArrayList<NeighborConnection> outgoingNeighborConnections=null;
		private ConcurrentLinkedQueue<NWResponse> outgoingResponseQueue=null;
		private ConcurrentLinkedQueue<ClientResponseWrapper> outgoingClientResponseQueue=null;
		
		public ArrayList<NeighborConnection> getOutgoingNeighborConnections() {
			return outgoingNeighborConnections;
		}
		
		public NodeResponseModule(List<String> neighborList,int neighborResponseServerPort){
			
			this.outgoingNeighborConnections=getConnectionList(neighborList,neighborResponseServerPort);
			outgoingClientResponseQueue=new ConcurrentLinkedQueue<ClientResponseWrapper>();
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
		
		public void insertOutgoingClientResponse(ClientResponseWrapper response){
			
		}
		
		private void initializeQueue(){
			this.outgoingResponseQueue=new ConcurrentLinkedQueue<NWResponse>();
		}


}
