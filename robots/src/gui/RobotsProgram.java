package gui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//          UIManager.setLookAndFeel(UIsManager.getSystemLookAndFeelClassName());
//          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
          e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame;
			try {
				frame = new MainApplicationFrame();
			
				frame.addWindowListener(new WindowAdapter() {
	        	    public void windowClosing(WindowEvent event) {
					    Object[] options = { "Да", "Нет!" };
					    int n = JOptionPane
					    		.showOptionDialog(event.getWindow(), "Закрыть окно?",
					    				"Подтверждение", JOptionPane.YES_NO_OPTION,
					    				JOptionPane.QUESTION_MESSAGE, null, options,
					    				options[0]);
					  if (n == 0) {
						  event.getWindow().setVisible(false);
						  WindowSerializer.saveWindowState(frame.logWindow, "log_window.bin");
						  WindowSerializer.saveWindowState(frame.gameWindow, "game_window.bin");
						  frame.unregister();
						  System.exit(0);
					  }
					  frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				    }
	            });
  
	            frame.pack();
	            frame.setVisible(true);
	            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	        } catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		});
    }
}
