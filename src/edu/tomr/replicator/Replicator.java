package edu.tomr.replicator;

/*
 * This can have different specializations to hold primary and secondary replica nodes
 * Also something that can be used by the primary node to support primary replica input connections.
 */
public class Replicator {
	
	//Replicator has a  data structure which stores the operations that occurred on the node data
	//Replica connects to the main node's replicator and uses the data structure to recreate the changes in
	//its own map.
	
	public void initReplicator(){
		
	}
	
	public void initConnection(){
		//Connection from replica node to the main node
		//Main node: listens for a connection and accepts a connection from the replica
	}
	
	/*public void initConnection(){
		//Connection from replica node to the main node
	}*/
}
