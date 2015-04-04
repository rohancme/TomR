package network.requests;

import static network.NetworkConstants.Requests;

public class NewNeighborConnection extends NWRequest {

	public NewNeighborConnection(String req_id) {
		super(req_id, Requests.NEW_CLIENT_CONNECTION);
	}

}
