package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class NeighborConnection {
	
	Socket socket;
	
	public NeighborConnection(String IP_Address, int port_num){
		update_neighbor(IP_Address,port_num);
	}
	
	private void update_neighbor(String IP_Address,int port_num){
		try {
			socket =new Socket(IP_Address,port_num);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host:"+IP_Address);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Some kind of IO Exception at Host:"+IP_Address);
			e.printStackTrace();
		}
	}
	
	public void change_neighbor(String IP_Address,int port_num){
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error disconnecting from host:"+IP_Address);
			e.printStackTrace();
			return;
		}
		
		update_neighbor(IP_Address,port_num);
		
	}
	
	void send_request(NW_Request request){
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
			mapper.writeValue(System.out, request);
			mapper.writeValue(output_stream, request);
			output_stream.flush();
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
