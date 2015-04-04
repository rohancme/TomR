package network;

public final class NetworkConstants {

	public NetworkConstants() {
	}
	
	public static enum Requests{
		STARTUP,CLOSE,NEW_CLIENT_CONNECTION,DB_OPERATION,NEW_NEIGHBOR_CONNECTION
	}
	
	public static String requestToString(Requests req){
		
		switch(req){
			case CLOSE: return "CLOSE";
			case NEW_CLIENT_CONNECTION: return "NEW_CLIENT_CONNECTION";
			case DB_OPERATION: return "DB_OPERATION";
			case NEW_NEIGHBOR_CONNECTION: return "NEW_NEIGHBOR_CONNECTION";
			case STARTUP: return "STARTUP";
		}
		
		return null;
	}

}
