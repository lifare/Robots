package log;

public enum LogLevel {
	TRACE(0),
	DEBUG(1),
	INFO(2),
	WARNING(3),
	ERROR(4),
	FATAL(5);

	private int level;
	
	private LogLevel(int level) {
		this.level = level;
	}
	
	public int level() {
		return level;
	}
}

