package gui;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogEntry;

public class WindowСoordinate extends JInternalFrame  implements Observer {
	
    private TextArea content;
	
	public WindowСoordinate() {
		super("Координаты робота", true, true, true);
        content = new TextArea("");
        content.setSize(250, 450);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(content, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
	}
   
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof ModelRobots) {
			ModelRobots arg = (ModelRobots)arg0;
			StringBuilder content = new StringBuilder();
			content.append(this.content.getText()).append(arg.getDRobotPositionX()).append(' ').append(arg.getDRobotPositionY()).append("\n");
	        this.content.setText(content.toString());
	        this.content.invalidate();	
		
		}
	}

}
