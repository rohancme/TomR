package edu.tomr.node.base;

import java.util.HashMap;
import java.util.Map;

import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.node.map.operations.MapOperation;

/*
 * Should containing a network module to handle the connections
 * Can add appropriate constructors to initialize n/w module variables
 */
public class Node {
	
	private Map<String, byte[]> inMemMap;
	private IMapOperation operation;
	
	public Node(){
		inMemMap = new HashMap<String, byte[]>();
		operation = new MapOperation(inMemMap);
	}
	
}
