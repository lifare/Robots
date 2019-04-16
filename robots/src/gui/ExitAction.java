package gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


class ExitAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;
    ExitAction() {
        putValue(NAME, "Выход");
    }
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}