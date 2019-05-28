package log;

import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogWindowSource {
	private int iQueueLength;
	private volatile int currentQueueSize;
	private ConcurrentLinkedQueue<LogEntry> messages;
	private final ConcurrentLinkedQueue<LogChangeListener> listeners;
	private volatile LogChangeListener[] activeListeners;
	
	public LogWindowSource(int iQueueLength) {
		this.iQueueLength = iQueueLength;
	    this.messages = new ConcurrentLinkedQueue<LogEntry>();
	    this.listeners = new ConcurrentLinkedQueue<LogChangeListener>();
	    this.currentQueueSize = 0;
	}
	
	public void registerListener(LogChangeListener listener) {
    	this.listeners.add(listener);
        this.activeListeners = null;
	}
	
	public void unregisterListener(LogChangeListener listener) {
		this.listeners.remove(listener);
		this.activeListeners = null;
	}
	
	public void append(LogLevel logLevel, String strMessage) {
		LogEntry entry = new LogEntry(logLevel, strMessage);
		if (this.currentQueueSize >= this.iQueueLength) {
			this.messages.offer(entry);
		}
		else {
			this.messages.add(entry);
			this.currentQueueSize++;
		}
		LogChangeListener [] activeListeners = this.activeListeners;
		if (activeListeners == null) {
		    if (this.activeListeners == null) {
		    	activeListeners = this.listeners.toArray(new LogChangeListener [0]);
		        this.activeListeners = activeListeners;
			}
		}
		for (LogChangeListener element : activeListeners) {
			element.onLogChanged();
		}
	}
	
	public int size() {
		return this.currentQueueSize;
	}
	
	public Iterable<LogEntry> range(int startFrom, int count) {
	    if (startFrom < 0 || startFrom >= this.currentQueueSize) {
	    	return Collections.emptyList();
	    }
	    int indexTo = Math.min(startFrom + count, this.currentQueueSize);
	    ConcurrentLinkedQueue<LogEntry> result = new ConcurrentLinkedQueue<LogEntry>();
	    int i = 0;
	    for(LogEntry e : this.messages) {
	    	if (i == indexTo) {
	    		result.add(e);
	    		break;
	    	}
	    	if(i >= startFrom) {
	    		result.add(e);
	    	}
	    	i++;
	    }
	    return result;
	}
	
	public Iterable<LogEntry> all() {
		return this.messages;
	}
}
