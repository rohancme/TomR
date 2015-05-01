package network.responses.outgoing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.utils.Constants;
import network.responses.ClientResponseWrapper;
import network.responses.NWResponse;

public class OutgoingClientResponseServicer implements Runnable {

	private ConcurrentLinkedQueue<ClientResponseWrapper> responseQueue=null;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!responseQueue.isEmpty()){
				ClientResponseWrapper responseWrapper=responseQueue.poll();
				sendResponse(responseWrapper.getSocket(),responseWrapper.getResponse());
			}
		}
	}
	
	public OutgoingClientResponseServicer(ConcurrentLinkedQueue<ClientResponseWrapper> responseQueue){
		this.responseQueue=responseQueue;
	}
	
	private void sendResponse(Socket socket,NWResponse response) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		DataOutputStream output_stream=null;
		try {
			output_stream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Constants.globalLog.debug("Unable to open output stream to host:"+socket.getInetAddress());
			e.printStackTrace();
			return;
		}
		
		try {
			//mapper.writeValue(System.out, request);
			mapper.writeValue(output_stream, response);
			//end of message marker.
			output_stream.writeChar('\n');
			output_stream.flush();
		} catch (JsonGenerationException e) {
			Constants.globalLog.debug("Problem Generating JSON");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			Constants.globalLog.debug("Problem with JSON mapping");
			e.printStackTrace();
		} catch (IOException e) {
			Constants.globalLog.debug("Problem with IO with host:"+socket.getInetAddress());
			e.printStackTrace();
		}
		
		
		//close the connection
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constants.globalLog.error("Couldn't close the Client Socket");
		}
	}

}
