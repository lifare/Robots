package gui;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

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
	public void update(Observable robot, Object arg1) {
		if (robot instanceof ModelRobots) {
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			ModelRobots arg = (ModelRobots)robot;
			StringBuilder content = new StringBuilder();
			String x = df.format(arg.getDRobotPositionX());
			String y = df.format(arg.getDRobotPositionY());
			content.append("X: ").append(x).append(' ').append(" Y :").append(y).append("\n");
	        this.content.setText(content.toString());
	        this.content.invalidate();	
		
		}
	}

}
