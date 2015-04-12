package edu.tomr.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue<T> {

	private Queue<T> queue;
		
	public MessageQueue() {
		queue = new ConcurrentLinkedQueue<T>();
	}
	
	public boolean queueMessage(T message) {
		return queue.add(message);
	}
	
	public T dequeueMessage() {
		
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
