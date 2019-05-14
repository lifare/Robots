package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

class ExitAction extends AbstractAction {
	private static final long serialVersionUID = -2860012103966789279L;
	private MainApplicationFrame frame;
    
    public ExitAction(MainApplicationFrame frame) {
        putValue(NAME, "Выход");
        this.frame = frame;
    }
    
    public void actionPerformed(ActionEvent e) {
    	Object[] options = { "Да", "Нет" };
		int n = JOptionPane
			.showOptionDialog(this.frame, "Закрыть окно?",
				"Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if (n == 0) {
			WindowSerializer.saveWindowState(frame.logWindow, "log_window.bin");
			WindowSerializer.saveWindowState(frame.gameWindow, "game_window.bin");
			frame.unregister();
			System.exit(0);
		}
	}
 }