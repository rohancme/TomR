package edu.tomr.node.map.operations;

import java.util.Map;
import java.util.Set;

public class MapOperation implements IMapOperation {
	
	private Map<String, byte[]> memoryMap;
	
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

	@Override
	public Set<String> getKeySet() {

		return this.memoryMap.keySet();
	}
}
