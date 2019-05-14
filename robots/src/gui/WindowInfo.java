package gui;

import java.io.Serializable;

public class WindowInfo implements Serializable {
	private static final long serialVersionUID = 3777391221417393523L;
	public int xPosition;
    public int yPosition;

    public int width;
    public int height;

    public boolean isMinimized;
    public boolean isMaximized;
    
    public WindowInfo() {}
}
