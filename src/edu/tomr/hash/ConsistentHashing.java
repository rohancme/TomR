package edu.tomr.hash;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class ConsistentHashing {
	static TreeMap<Double, String> unitCircle = new TreeMap(); 
	
	//Method to hash the Nodes onto the unit circle
	public static void calculateCircle(ArrayList<String> nodes){
		
		for(String node : nodes){
			unitCircle.put(hashFunction1(node), node);
			unitCircle.put((hashFunction1(node) + .3)%.99, node);
			unitCircle.put((hashFunction1(node) + .6)%.99, node);
		}
		
		
	}
	
	public static TreeMap<Double, String> getCircle(ArrayList<String> nodes){
		calculateCircle(nodes);
		return unitCircle;
		
	}
	
	public static String getNode(String key){
		Double hashOfKey = hashFunction2(key);
		Double nodeNumber = getFirstNode(hashOfKey, unitCircle);
		String nodeName = unitCircle.get(nodeNumber);
		return nodeName;
		
	}
	
	//Method to hash the keys onto the Nodes	
	static Map<String, String> hashKeys(Map<String, String> keyMap, ArrayList<String> keys, TreeMap<Double, String> circle, Map<String, String> changeMap){
		for(String key : keys){
			Double hashOfKey = hashFunction2(key);
			//System.out.println(key + "->" + hashOfKey);
			Double nodeNumber = getFirstNode(hashOfKey, circle);
			String nodeName = circle.get(nodeNumber);
			String previousNode;
			if(keyMap.containsKey(key)){
				previousNode = keyMap.get(key);
				if(!(previousNode.equals(nodeName))){
					changeMap.put(key, nodeName);
				}
			}
			keyMap.put(key, nodeName);
		}
		return keyMap;
		
	}
	
	//Method to get the nearest node to the key
	static Double getFirstNode(Double key, TreeMap<Double, String> circle ){
		Double firstKey = 0.0;
		for(Entry<Double, String> entry : circle.entrySet()) {
			Double value = entry.getKey();
			if(value < key){
				if(value > firstKey){
					firstKey = value;
				}
				
			}
		}
		
		return firstKey;
		
	}
	
	static double hashFunction1(String newString){
		Double hash=7.5;
		for (int i=0; i < newString.length(); i++) {
		    hash = hash*31+newString.charAt(i);
		}
		//Double num = Math.pow(10, (Math.log10(hash)+1));
		return hash%.99;
		
	}
	
	static double hashFunction2(String newString){
		Double hash=7.5;
		for (int i=0; i < newString.length(); i++) {
		    hash = hash*31+newString.charAt(i);
		}
		//Double num = Math.pow(10, (Math.log10(hash)+1));
		return hash%.99;
		
	}
	
	static Map<String, String> addNode(ArrayList<String> nodes, String node, TreeMap<Double, String> circle,  Map<String, String> keyMap, Map<String, String> changeMap, ArrayList<String> keys){
		nodes.add(node);
		circle = reComputeHash(nodes);
		keyMap = hashKeys(keyMap, keys, circle, changeMap);
		return keyMap;
		
	}
	
	static Map<String, String> deleteNode(ArrayList<String> nodes, int index, TreeMap<Double, String> circle,  Map<String, String> keyMap, Map<String, String> changeMap, ArrayList<String> keys){
		nodes.remove(index);
		circle = reComputeHash(nodes);
		keyMap = hashKeys(keyMap, keys, circle, changeMap);
		return keyMap;
		
	}
	private static TreeMap<Double, String> reComputeHash(ArrayList<String> nodes) {
		TreeMap<Double, String> unitCircle = new TreeMap<>();
		calculateCircle(nodes);
		return unitCircle;
	}

	

}
