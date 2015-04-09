package edu.tomr.hash;

import java.util.List;
import java.util.Map;

public interface IConsistentHashing {

	public abstract Map<Double, String> calculateCircle(List<String> list);
	
	public abstract Map<Double, String> getCircle();
	
	public abstract String calculateNode(String key);
}
