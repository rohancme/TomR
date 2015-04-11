package edu.tomr.network.heartbeat.base;

public final class ConnectionAddress {

	private String ipAddress;
	private int portNumber;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public ConnectionAddress() {}
	
	public ConnectionAddress(String ipAddress, int portNumber) {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		ConnectionAddress address = (ConnectionAddress)obj;
		if(this.ipAddress.equalsIgnoreCase(address.getIpAddress()) &&
				this.portNumber == address.getPortNumber())
			return true;
		
		return false;
	}
	
	
	
}
