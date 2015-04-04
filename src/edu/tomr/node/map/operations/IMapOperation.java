package edu.tomr.node.map.operations;

public interface IMapOperation {

	public byte[] get(String key);
	
	public void put(String key, byte[] value);
	
	public byte[] delete(String key);
}
