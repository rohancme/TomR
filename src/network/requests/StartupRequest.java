package network.requests;

import edu.tomr.protocol.Message;
import network.NetworkConstants.Requests;

public class StartupRequest extends NWRequest{

	public StartupRequest(String req_id,Message msg) {
		super(req_id, Requests.STARTUP,msg);
	}

}
