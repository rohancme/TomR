package edu.tomr.protocol;

import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class Message {
	
	public void toJSON(OutputStream stream, Message message) throws JsonGenerationException, 
		JsonMappingException, IOException {
		
		new ObjectMapper().writeValue(stream, message);
	}
}
