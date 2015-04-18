package edu.tomr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigParams {

	static Properties prop = new Properties();
	private static final String propertyFileName = "tomr.config";
	private static final String tomrEnvVar = "TOMR_PROPS";

	public static void loadProperties() {

		String propFilePath = System.getenv(tomrEnvVar);


		StringBuilder builder = new StringBuilder();
		builder.append(propFilePath).append(propertyFileName);

		InputStream input = null;
		try {
			input = new FileInputStream(builder.toString());
		} catch (FileNotFoundException e) {

			System.out.println("property file '" + builder.toString() + "' not found ");
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			prop.load(input);
		} catch (IOException e) {
			System.out.println("Problem loading property file '" + builder.toString());
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static String getProperty(String key) {

		String val = prop.getProperty(key);
		if(null != val)
			return val.trim();

		return null;
	}

	public static int getIntProperty(String key) {

		String val = prop.getProperty(key);
		if(null != val)
			return Integer.parseInt(val.trim());

		return -1;
	}

	public static List<String> getIpAddresses() {

		String ipAdds = prop.getProperty("Node_IP_Addresses");

		String[] ipAddsArray = ipAdds.split(",");

		List<String> list = new ArrayList<String>();
		for(int i=0; i<ipAddsArray.length; ++i) {
			if(ipAddsArray[i] != null)
				list.add(ipAddsArray[i].trim());
		}

		return list;
	}

	public static void addIpAddress(String ipAddress) {

		String nodes = prop.getProperty("Node_IP_Addresses");
		StringBuilder sBuilder = new StringBuilder(prop.getProperty("Node_IP_Addresses"));
		if(nodes.lastIndexOf(',') != nodes.trim().length()-1)
			sBuilder.append(',');
		sBuilder.append(ipAddress);
		prop.setProperty("Node_IP_Addresses", sBuilder.toString());

		String propFilePath = System.getenv(tomrEnvVar);
		StringBuilder builder = new StringBuilder();
		builder.append(propFilePath).append(propertyFileName);

		OutputStream output = null;
		try {
			output = new FileOutputStream(new File(builder.toString()));
			prop.store(output, "Added new IP Address");
		} catch (FileNotFoundException e) {

			System.out.println("property file '" + builder.toString() + "' not found ");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {

			System.out.println("Could not add IP Address to config file");
			e.printStackTrace();
		}
	}

	public static void removeIpAddress(String ipAddress) {

		String nodes = prop.getProperty("Node_IP_Addresses");

		Pattern p = Pattern.compile(ipAddress+"+(\\s)?+(,)?");
		Matcher m = p.matcher(nodes);

		if(m.find()){
			 String updateNodes = nodes.replace(nodes.substring(m.start(), m.end()), "");
			 prop.setProperty("Node_IP_Addresses", updateNodes);
		}

		String propFilePath = System.getenv(tomrEnvVar);
		StringBuilder builder = new StringBuilder();
		builder.append(propFilePath).append(propertyFileName);

		OutputStream output = null;
		try {
			output = new FileOutputStream(new File(builder.toString()));
			prop.store(output, "Removed IP Address");
		} catch (FileNotFoundException e) {

			System.out.println("property file '" + builder.toString() + "' not found ");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {

			System.out.println("Could not remove IP Address from config file");
			e.printStackTrace();
		}
	}

	public static String getRandomIpAddress(){

		List<String> ips = getIpAddresses();

		Random rand = new Random();
		int  n = rand.nextInt(ips.size()-1);

		return ips.get(n);
	}

	/*public static void main(String[] args) {

		ConfigParams config = new ConfigParams();
		config.loadProperties();


		addIpAddress("1.2.3.4");
		loadProperties();

		Enumeration<String> enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}

		removeIpAddress("1.2.3.4");
		loadProperties();

		enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}
		System.out.println(">>>>>>>> "+getIpAddresses()+" \n\n\n");


		addIpAddress("1.2.3.4");
		loadProperties();

		enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}

		removeIpAddress("192.168.1.103");
		loadProperties();

		enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}
		System.out.println(">>>>>>>> "+getIpAddresses()+" \n\n\n");

		addIpAddress("192.168.1.103");
		loadProperties();

		enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}

		removeIpAddress("1.2.3.4");
		loadProperties();

		enumeration = (Enumeration<String>) config.prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}
		System.out.println(">>>>>>>> "+getIpAddresses()+" \n\n\n");

	}*/

}
