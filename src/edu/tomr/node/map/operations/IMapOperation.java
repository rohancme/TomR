package edu.tomr.node.map.operations;

import java.util.Map;

public interface IMapOperation {

	@Deprecated
	public void setMemoryMap(Map<String, byte[]> memoryMap); 
	
	public byte[] get(String key);
	
	public void put(String key, byte[] value);
	
	public byte[] delete(String key);
}
