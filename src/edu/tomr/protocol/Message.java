package edu.tomr.protocol;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class Message {
	
	static StringWriter writer;
	
	public String toJSON(Message message) throws JsonGenerationException, 
		JsonMappingException, IOException {
		
		writer = new StringWriter();
		new ObjectMapper().writeValue(writer, message);
		
		return writer.toString();
	}
}
