package gui;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JInternalFrame;

public class WindowSerializer {
	public static void saveWindowState(JInternalFrame frame, String name) {
		WindowInfo info = new WindowInfo();
		info.height = frame.getHeight();
    	info.width = frame.getWidth();
    	info.xPosition = frame.getX();
    	info.yPosition = frame.getY();
    	info.isMaximized = frame.isMaximum();
    	info.isMinimized = frame.isIcon();
    	serialize(info, name);
	}
	
	private static void serialize(WindowInfo info, String name) {
		File file = new File(name);
		try (OutputStream os = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
			oos.writeObject(info);
			oos.flush();
		} catch (IOException ex) { 
			ex.printStackTrace();
		}
	}
	
	private static WindowInfo deserialize(String name) {
		WindowInfo restored = null;
		try (InputStream is = new FileInputStream(name);
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
			restored = (WindowInfo)ois.readObject();
				} catch (ClassNotFoundException | IOException ex) { 
					ex.printStackTrace(); 
				}
		return restored;
	}
	
	public static JInternalFrame loadWindowState(String name, JInternalFrame frame) throws PropertyVetoException {
		WindowInfo info = deserialize(name);
		if (info != null) {
    		frame.setLocation(info.xPosition, info.yPosition);
    		frame.setSize(info.width, info.height);
    		frame.setIcon(info.isMinimized);
    		frame.setMaximum(info.isMaximized);
    	}
		return info != null ? frame : null;
	}
}
