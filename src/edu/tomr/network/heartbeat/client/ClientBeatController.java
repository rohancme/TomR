package edu.tomr.network.heartbeat.client;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.tomr.network.base.ConnectionAddress;

public class ClientBeatController extends Thread {

	private ConnectionAddress serverAddress;
	private ConnectionAddress selfAddress;
	private Client clientBeater;
	
	private ClientBeatController(String serverIp, int serverPortNo) {
		
		this.serverAddress = new ConnectionAddress(serverIp, serverPortNo);
	}
	
	public ClientBeatController(String serverIp, int serverPortNo, String selfIp,
			int selfPortNo) {
		
		this(serverIp, serverPortNo);
		this.selfAddress = new ConnectionAddress(selfIp, selfPortNo);
	}

	public void initSendClose() throws InterruptedException, IOException {
		this.initializeClientBeater();
		this.clientBeater.sendHeartBeat(this.selfAddress);
		this.clientBeater.closeConnection();
	}
	
	public void initializeClientBeater() {
		clientBeater = new Client(this.serverAddress);
		try {
			clientBeater.initiateConnection();
		} catch (UnknownHostException e) {
			System.out.println("initializeClientBeater: host exception in cleint");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("initializeClientBeater: IO exception in cleint");
			e.printStackTrace();
		}
	}
	
	public void startHeartBeats() throws InterruptedException {
		while(true) {
			this.run();
			Thread.sleep(5000);
		}
	}
	
	public void run() {
		try {
			this.initSendClose();
		} catch (IOException e) {
			System.out.println("run: IO exception while sending heartbeat");
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String serverIp = "", selfIp = args[0];
		int serverPortNo = 8080, selfPortNo = Integer.parseInt(args[1]);
		
		ClientBeatController clientController = new ClientBeatController(serverIp, serverPortNo, selfIp, selfPortNo);
		/*clientController.initializeClientBeater();
		try {
			clientController.startHeartBeats();
		} catch (InterruptedException e) {
			System.out.println("client main: Interrupted exception");
			e.printStackTrace();
		} finally{
			try {
				clientController.clientBeater.destory();
			} catch (IOException e) {
				System.out.println("client main: finally: IO exception");
				e.printStackTrace();
			}
		}*/
		try {
			clientController.startHeartBeats();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
