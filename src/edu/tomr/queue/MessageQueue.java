package edu.tomr.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.tomr.protocol.Message;

public class MessageQueue {

	private Queue<Message> queue;
		
	public MessageQueue() {
		queue = new ConcurrentLinkedQueue<Message>();
	}
	
	public boolean queueMessage(Message message) {
		return queue.add(message);
	}
	
	public Message dequeueMessage() {
		
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
