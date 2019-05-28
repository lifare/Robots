package log;

public class LogEntry {
	private LogLevel logLevel;
	private String strMessage;
    
	public LogEntry(LogLevel logLevel, String strMessage) {
		this.strMessage = strMessage;
		this.logLevel = logLevel;
	}
    
	protected LogEntry() {}
	
	public String getMessage() {
		return this.strMessage;
	}

	public LogLevel getLevel() {
		return this.logLevel;
	}
}

