package gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

class ExitAction extends AbstractAction {
	private MainApplicationFrame frame;
    private static final long serialVersionUID = 1L;
    
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
			frame.unregister();
			System.exit(0);
		}
	}
 }