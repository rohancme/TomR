package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.protocol.Message;

public class Simple_Server {
	
	ServerSocket socket;
	
	Simple_Server(int port_num){
		try {
			socket=new ServerSocket(port_num);
		} catch (IOException e) {
			System.out.println("Error creating socket at port:"+port_num);
			e.printStackTrace();
		}
	}
	
	public String listen_and_print() throws JsonGenerationException, JsonMappingException, IOException{
		
		Socket client_socket=null;
		
		try {
			client_socket=socket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc=null;
		ObjectMapper mapper = new ObjectMapper();
		Message msg=null;
		try {
			msg=mapper.readValue(client_socket.getInputStream(),Message.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*StringBuilder sb=new StringBuilder();
		
		while(sc.hasNext()){
			sb.append(sc.next());
		}
		
		
		return sb.toString();*/
		System.out.println(msg.toJSON(msg));
		
		return null;
	}

}
