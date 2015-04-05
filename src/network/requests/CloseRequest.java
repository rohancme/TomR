package network.requests;
import static network.NetworkConstants.Requests;
import edu.tomr.protocol.Message;

public class CloseRequest extends NWRequest {
	
	public CloseRequest(String req_id, Message msg){
		super(req_id, Requests.CLOSE,msg);
	}

}
