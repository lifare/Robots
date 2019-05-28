package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener {
	private static final long serialVersionUID = 3146835153013483557L;
	private LogWindowSource logSource;
	private TextArea logContent;
    
    protected LogWindow() {
    	super();
    }

    public LogWindow(LogWindowSource logSource) {
    	super();
    	this.title = "Протокол работы";
    	this.resizable = true;
    	this.closable = true;
    	this.maximizable = true;
    	this.iconable = true;
    	
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea();
        logContent.setText("");
        logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    public void exit() {
        logSource.unregisterListener(this);
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }
    
    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
