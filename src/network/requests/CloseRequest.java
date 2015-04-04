package network.requests;
import static network.NetworkConstants.Requests;

public class CloseRequest extends NWRequest {
	
	public CloseRequest(String req_id){
		super(req_id, Requests.CLOSE);
	}

}
