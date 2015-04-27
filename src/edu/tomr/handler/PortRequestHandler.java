package edu.tomr.handler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.tomr.utils.Constants;

public class PortRequestHandler<T> implements Runnable {

	private int port;
	private String handlerName;

	public PortRequestHandler(int port, String handlerName) {
		this.port = port;
		this.handlerName = handlerName;
	}

	@Override
	public void run() {

		Constants.globalLog.debug("started listening for connections");
		ServerSocket server = null;
		try {
			server = new ServerSocket(this.port);
			while (true) {
				//Runnable runner = (Runnable)Class.forName(handlerName).getConstructor()newInstance();
				@SuppressWarnings("unchecked")
				Constructor<T> c = (Constructor<T>) Class.forName(handlerName).getConstructor(Socket.class);
				T runner = c.newInstance(server.accept());
				Thread t = new Thread((Runnable)runner);
				t.start();
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
