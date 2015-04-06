package network.requests;

import edu.tomr.protocol.Message;
import network.NetworkConstants.Requests;

public class DBRequest extends NWRequest{

	public DBRequest(String req_id,Message msg) {
		super(req_id, Requests.DB_OPERATION,msg);
	}

}
