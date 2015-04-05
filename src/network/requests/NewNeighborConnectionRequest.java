package network.requests;

import static network.NetworkConstants.Requests;
import edu.tomr.protocol.Message;

public class NewNeighborConnectionRequest extends NWRequest {

	public NewNeighborConnectionRequest(String req_id,Message msg) {
		super(req_id, Requests.NEW_NEIGHBOR_CONNECTION,msg);
	}

}
