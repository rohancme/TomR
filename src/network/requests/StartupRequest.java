package network.requests;

import network.NetworkConstants.Requests;

public class StartupRequest extends NWRequest {

	public StartupRequest(String req_id) {
		super(req_id, Requests.STARTUP);
	}

}
