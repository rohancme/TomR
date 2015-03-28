package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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
	
	public String listen_and_print(){
		
		Socket client_socket=null;
		
		try {
			client_socket=socket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc=null;
		try {
			sc=new Scanner(client_socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder sb=new StringBuilder();
		
		while(sc.hasNext()){
			sb.append(sc.next());
		}
		
		return sb.toString();
	}

}
