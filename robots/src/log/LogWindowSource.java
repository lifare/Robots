package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ArrayDeque;

public class LogWindowSource {
	private int iQueueLength;
	
	private ArrayDeque<LogEntry> messages;
	private final ArrayList<LogChangeListener> listeners;
	private volatile LogChangeListener[] activeListeners;
	
	public LogWindowSource(int iQueueLength) {
		this.iQueueLength = iQueueLength;
	    this.messages = new ArrayDeque<LogEntry>(iQueueLength);
	    this.listeners = new ArrayList<LogChangeListener>();
	}
	
	public void registerListener(LogChangeListener listener) {
		synchronized(this.listeners) {
	    	this.listeners.add(listener);
	        this.activeListeners = null;
		}
	}
	
	public void unregisterListener(LogChangeListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
			this.activeListeners = null;
		}
	}
	
	public void append(LogLevel logLevel, String strMessage) {
		LogEntry entry = new LogEntry(logLevel, strMessage);
		if (this.messages.size() >= this.iQueueLength) {
			this.messages.poll();
		}
		this.messages.add(entry);
		LogChangeListener [] activeListeners = this.activeListeners;
		if (activeListeners == null) {
			synchronized (this.listeners) {
			    if (this.activeListeners == null) {
			    	activeListeners = this.listeners.toArray(new LogChangeListener [0]);
			        this.activeListeners = activeListeners;
			    }
			}
		}
		for (LogChangeListener element : activeListeners) {
			element.onLogChanged();
		}
	}
	
	public int size() {
		return this.messages.size();
	}
	
	public Iterable<LogEntry> range(int startFrom, int count) {
	    if (startFrom < 0 || startFrom >= this.messages.size()) {
	    	return Collections.emptyList();
	    }
	    int indexTo = Math.min(startFrom + count, this.messages.size());
	    ArrayList<LogEntry> result = new ArrayList<LogEntry>();
	    var i = 0;
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
