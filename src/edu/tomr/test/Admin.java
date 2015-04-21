package edu.tomr.test;

import network.Connection;
import network.NetworkConstants;
import network.NetworkUtilities;
import network.exception.NetworkException;
import network.requests.NWRequest;
import edu.tomr.protocol.UpdateConnMessage;

public class Admin {

	public static void main(String[] args) {
		
		Connection conn=new Connection("192.168.1.103", NetworkConstants.LB_ADD_LISTEN_PORT);
		
		NetworkUtilities utils = null;
		try {
			utils = new NetworkUtilities();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NWRequest request = utils.getNewAddNodeRequest(new UpdateConnMessage("ADD_NODE", utils.getSelfIP(), true));
		
		conn.send_request(request);
	}

}
