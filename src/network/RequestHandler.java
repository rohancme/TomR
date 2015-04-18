package network;

import java.io.IOException;

import network.requests.NWRequest;

import org.codehaus.jackson.map.ObjectMapper;
//retrieves the next incoming request
public class RequestHandler extends PersistentIncomingConnectionHandler {
	
	protected RequestHandler(int incoming_port) {
		super(incoming_port);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Shifted this method here from ConnectionHandler so ConnectionHandler can be reused for responses 
	 */
	protected NWRequest getNextRequest(){
		
		ObjectMapper mapper = new ObjectMapper();
		NWRequest request=null;
		//currently using scanner. Scanner waits for a newLine character which marks the end of an object
		if(inputScanner.hasNextLine()){
			try {
				request=mapper.readValue(inputScanner.nextLine(), NWRequest.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return request;
	}
}
