package edu.tomr.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
			list.add(ipAddsArray[i].trim());
		}
		
		return list;
	}
	
	/*public static void main(String[] args) {
		
		ConfigParams config = new ConfigParams();
		config.loadProperties();
		
		Enumeration<String> enumeration = (Enumeration<String>) config.prop.propertyNames(); 
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key: "+key+" value: "+config.prop.getProperty(key));
		}
	}*/

}
