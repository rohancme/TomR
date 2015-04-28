package edu.tomr.replica.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import network.exception.NetworkException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.tomr.replica.protocol.AbsReplicaMessage;
import edu.tomr.utils.Constants;

public class ReplicaConnectionHandler {

	protected Socket clientSocket;
	protected InputStream inputStream=null;
	protected Scanner inputScanner=null;

	private OutputStream outStream=null;

	public ReplicaConnectionHandler(int incoming_port) {


	}

	public void initSocket(ServerSocket serverSocket) throws NetworkException {

		try {
			clientSocket=serverSocket.accept();

			inputStream=clientSocket.getInputStream();
			inputScanner=new Scanner(inputStream);

			outStream = clientSocket.getOutputStream();

		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException("Error accepting incoming connections\n");
		}
	}

	public void sendMessage(AbsReplicaMessage msg) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		DataOutputStream output_stream=null;
		output_stream= new DataOutputStream(outStream);

		try {
			//mapper.writeValue(System.out, request);
			mapper.writeValue(output_stream, msg);
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
			Constants.globalLog.debug("Problem with IO with host:"+clientSocket.getInetAddress());
			e.printStackTrace();
		}
	}



	protected void closeClientSocket() throws NetworkException{
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException("There was an error closing the client socket\n");
		}
	}

}
