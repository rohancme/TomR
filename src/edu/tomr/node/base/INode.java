package edu.tomr.node.base;

import edu.tomr.protocol.AckMessage;
import edu.tomr.protocol.DBMessage;
import edu.tomr.protocol.RedistributionMessage;
import edu.tomr.protocol.UpdateRingMessage;

public interface INode {

	/**
	 * For client requests
	 * @param message DBMessage from client
	 */
	public void handleRequest(DBMessage message);

	/**
	 * For node requests
	 * @param message DBMessage from client
	 * @param originalServicerIP : Original node IP Address servicing the request
	 */
	public void handleRequest(DBMessage message, String originalServicerIP);

	/**
	 * Only for node acknowledgments
	 * @param ackMessage
	 */
	public void handleAcknowledgements(AckMessage ackMessage);

	/**
	 * Handler for Update Ring Mesages
	 * @param message
	 */
	public void handleUpdateRingRequest(UpdateRingMessage message);

	/**
	 * Handler for redistribution messages
	 * @param message
	 */
	public void redistributionRequest(RedistributionMessage message);

}
