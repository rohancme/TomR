package edu.tomr.protocol;

public class ClientInfo {

	@JsonProperty private final String ipAddress;
	@JsonProperty private int port;

	public ClientInfo(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public ClientInfo(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIpAddress() {
		return ipAddress;
	}


}
