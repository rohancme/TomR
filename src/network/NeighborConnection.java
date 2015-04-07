package network;

import java.io.DataOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class NeighborConnection extends Connection{
	
	public NeighborConnection(String IP_Address, int port_num){
		super(IP_Address,port_num);
	}
	
	
	@SuppressWarnings("rawtypes")
	void send_request(NetworkPacket packet){
		ObjectMapper mapper = new ObjectMapper();
		DataOutputStream output_stream=null;
		try {
			output_stream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Unable to open output stream to host:"+socket.getInetAddress());
			e.printStackTrace();
			return;
		}
		
		try {
			mapper.writeValue(System.out, packet);
			mapper.writeValue(output_stream, packet);
		} catch (JsonGenerationException e) {
			System.out.println("Problem Generating JSON");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("Problem with JSON mapping");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problem with IO with host:"+socket.getInetAddress());
			e.printStackTrace();
		}
		
	}

}
