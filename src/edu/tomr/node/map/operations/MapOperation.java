package edu.tomr.node.map.operations;

import java.util.Map;

public class MapOperation implements IMapOperation {
	
	private Map<String, byte[]> memoryMap;
	
	@Deprecated
	public void setMemoryMap(Map<String, byte[]> memoryMap) {
		this.memoryMap = memoryMap;
	}

	public MapOperation(Map<String, byte[]> map) {
		this.memoryMap = map;
	}
	
	public byte[] get(String key) {
		return this.memoryMap.get(key);
	}
	
	public void put(String key, byte[] value) {
		
		if(value != null){
			this.memoryMap.put(key, value);
		}
	}
	
	public byte[] delete(String key) {
		return this.memoryMap.remove(key);
	}
}
