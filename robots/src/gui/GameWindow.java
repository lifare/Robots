package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {
	private static final long serialVersionUID = -346718258071455704L;
	private final GameVisualizer visualizer;
	
    public GameWindow() {
    	this.title = "Игровое поле";
    	this.resizable = true;
    	this.closable = true;
    	this.maximizable = true;
    	this.iconable = true;
    	
        visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
