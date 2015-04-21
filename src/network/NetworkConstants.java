package network;

public final class NetworkConstants {

	/*
	 * Client ports
	 */
	public static final int C_SERVER_LISTEN_PORT = 5000;
	
	//Client Heartbeat port
	public static final int C_BEAT_PORT = 5010;

	/*
	 * Server or Load Balancer ports
	 */
	public static final int LB_ADD_LISTEN_PORT = 8002;
	
	public static final int INCOMING_NEIGHBOR_PORT=5001;
	
	public static final int INCOMING_RESPONSE_PORT=5002;

	public NetworkConstants() {
	}

	public static enum Requests{

		STARTUP,CLOSE,NEW_CLIENT_CONNECTION,DB_OPERATION,NEW_NEIGHBOR_CONNECTION,NEIGHBOR_MESSAGE,SERVICE_MESSAGE
		,BREAK_FORM,BREAK_INCOMING_CONNECTION,UPDATE_RING,REDISTRIBUTION,ADD_NODE
	}

	public static String requestToString(Requests req){

		switch(req){
			case  NEIGHBOR_MESSAGE: return "NEIGHBOR_MESSAGE";
			case CLOSE: return "CLOSE";
			case NEW_CLIENT_CONNECTION: return "NEW_CLIENT_CONNECTION";
			case DB_OPERATION: return "DB_OPERATION";
			case NEW_NEIGHBOR_CONNECTION: return "NEW_NEIGHBOR_CONNECTION";
			case STARTUP: return "STARTUP";
			case SERVICE_MESSAGE: return "SERVICE_MESSAGE";
			case BREAK_FORM: return "BREAK_FORM";
			case BREAK_INCOMING_CONNECTION: return "BREAK_INCOMING_CONNECTION";
			case UPDATE_RING: return "UPDATE_RING";
			case REDISTRIBUTION: return "REDISTRIBUTION";
			case ADD_NODE: return "ADD_NODE";
		default:
			break;
		}

		return null;
	}

}
