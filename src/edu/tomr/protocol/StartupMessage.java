package edu.tomr.protocol;

import java.io.InputStream;
import java.util.Scanner;

public class StartupMessage extends Message {

	String msg;
	
	public StartupMessage(String msg){
		this.msg=msg;
	}

}
