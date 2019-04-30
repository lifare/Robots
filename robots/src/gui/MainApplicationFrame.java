package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.Logger;

public class MainApplicationFrame extends JFrame {
   
	private static final long serialVersionUID = 1L;
	private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);
        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();
        logWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                    super.internalFrameClosing(e);
                    logWindow.exit();
                    addOptionPane(e);
            }
        });
        addWindow(logWindow);
    	ModelRobots robot = new ModelRobots();
    	WindowСoordinate windowCoordinate = createWindowСoordinate();
    	robot.addObserver(windowCoordinate);
    	addWindow(windowCoordinate);
        GameWindow gameWindow = new GameWindow(robot);
        gameWindow.setSize(400,  400);
        gameWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e){
                    super.internalFrameClosing(e);
                    addOptionPane(e);
            }
        });
        addWindow(gameWindow);
        
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void addOptionPane(InternalFrameEvent e) {
        Object[] options = { "Да", "Нет" };
		int n = JOptionPane
				.showOptionDialog(e.getInternalFrame(), "Закрыть окно?",
						"Подтверждение", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options,
						options[1]);
		if (n == 0) {
			e.getInternalFrame().setVisible(false);
		}
		e.getInternalFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
    
    protected LogWindow createLogWindow() {
    	LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(400,GameWindow.HEIGHT);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected WindowСoordinate createWindowСoordinate() {
    	WindowСoordinate windowCoordinate = new WindowСoordinate();
    	windowCoordinate.setLocation(610, GameWindow.HEIGHT);
    	windowCoordinate.setSize(300, 500);
        setMinimumSize(windowCoordinate.getSize());
        windowCoordinate.pack();
        return windowCoordinate;
    }
    
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenu createMainMenu() {
        JMenu file = new JMenu("Меню");
        JMenuItem exit = new JMenuItem(new ExitAction(this));
        file.add(exit);
        return file;
    }
    
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = createMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");
        JMenuItem systemLookAndFeel = createMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createMenuItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);

        JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        JMenuItem addLogMessageItem = createMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> {
                Logger.debug("Новая строка");
            });
        testMenu.add(addLogMessageItem);
        
        menuBar.add(this.createMainMenu());
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }
    
    private JMenu createMenu(String name, int key, String description) {
    	JMenu menu = new JMenu(name);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }
    
    private JMenuItem createMenuItem(String name, int key, ActionListener listener) {
    	JMenuItem menuItem = new JMenuItem(name, key);
        menuItem.addActionListener(listener);
        return menuItem;
    }
    
    public void unregister() {
    	for(Object e : getFrames()) {
    		if (e instanceof LogWindow) {
    			((LogWindow)e).exit();
    		}
    	}
    }
    
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
