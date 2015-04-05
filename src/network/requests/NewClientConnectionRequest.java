package network.requests;
import static network.NetworkConstants.Requests;
import edu.tomr.protocol.Message;

public class NewClientConnectionRequest extends NWRequest {

	public NewClientConnectionRequest(String req_id,Message msg) {
		super(req_id, Requests.NEW_CLIENT_CONNECTION,msg);
		// TODO Auto-generated constructor stub
	}

}
