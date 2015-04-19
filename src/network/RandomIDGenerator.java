package network;

import java.util.UUID;

public class RandomIDGenerator {
	
	private final String prefix;
	
	public RandomIDGenerator(String prefix){
		this.prefix=prefix;
	}
	
	public String getRandomID(){
		return (prefix+UUID.randomUUID());
	}
}
