package edu.tomr.network.heartbeat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.protocol.HeartBeatMessage;

public class Server {

	private Controller controller;
	private int portNumber;
	private ServerSocket server;

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public Server(Controller controller, int portNumber) {
		this.controller = controller;
		this.portNumber = portNumber;
	}
	
	public void startServer() throws IOException{
		System.out.println("started listening for connections");
		server = new ServerSocket(this.portNumber);
		try {
            while (true) {
                new Handler(server.accept()).start();
            }
        } finally {
            server.close();
        }
	}
	
	private class Handler extends Thread {
		private Socket client;
		//private ConnectionAddress address;
		Scanner sc=null;
		
		public Handler(Socket client){
			this.client = client;
		}
		
		/*StringBuilder sb=new StringBuilder();*/
		
		/*while(sc.hasNext()){
			sb.append(sc.next());
		}*/
		
		public void run() {
			HeartBeatMessage message = null;
			BufferedReader in = null;
			try {
				//sc=new Scanner(client.getInputStream());
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e1) {
				System.out.println("Server:run buff reader");
				e1.printStackTrace();
			}
			
			//while(true){
                
				ObjectMapper mapper = new ObjectMapper();
                try {
                	
                	message = mapper.readValue(in, HeartBeatMessage.class);
				} catch (JsonParseException | JsonMappingException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("object deserialed error");
					e.printStackTrace();
				}
                if (message == null) {
                    return;
                }
                synchronized (controller.clients) {
                    
                	System.out.println("updated ip address and timestamp for server at: "
                			+message.toString()+" with time: "+message.getTimeStamp());
                	controller.clients.put(message.toString(), message.getTimeStamp());
                }
            //}
            try {
				client.close();
			} catch (IOException e) {
				System.out.println("client connection closed");
				e.printStackTrace();
			}
		}
	}
}
