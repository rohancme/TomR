package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import network.requests.NWRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonGenerator;

public class Connection {
	
	protected Socket socket;

	public Connection(String IP_Address, int port_num) {
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
	
	public Connection(Socket clientSocket){
		try {
			socket = clientSocket;
		} catch (Exception e) {
			System.out.println("Some kind of IO Exception: ");
			e.printStackTrace();
		}
	}
	
	public void send_request(NWRequest request){
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		DataOutputStream output_stream=null;
		try {
			output_stream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Unable to open output stream to host:"+socket.getInetAddress());
			e.printStackTrace();
			return;
		}
		
		try {
			//mapper.writeValue(System.out, request);
			mapper.writeValue(output_stream, request);
			//end of message marker.
			output_stream.writeChar('\n');
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
	
	public void send_response(NWResponse response) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		DataOutputStream output_stream=null;
		try {
			output_stream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Unable to open output stream to host:"+socket.getInetAddress());
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
