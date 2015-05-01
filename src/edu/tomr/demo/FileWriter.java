package edu.tomr.demo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.tomr.node.map.operations.IMapOperation;
import edu.tomr.utils.Constants;


public class FileWriter implements Runnable {

	private final String filePath;
	private final IMapOperation operation;
	
	public FileWriter(String filePath, IMapOperation operation) {
		this.filePath = filePath;
		this.operation = operation;
	}

	@Override
	public void run() {
		
		while(true){
			try {
				PrintWriter writer = new PrintWriter(this.filePath);
				
				Iterator<String> iter = this.operation.getKeySet().iterator();
				while (iter.hasNext()) {
					String string = (String) iter.next();
					writer.println(string);
				}
				writer.close();
				Thread.sleep(5000);
			} catch (FileNotFoundException | InterruptedException e) {
				Constants.globalLog.error("Error while writing keys to file "+this.filePath);
				e.printStackTrace();
			}
		}
	}

}
