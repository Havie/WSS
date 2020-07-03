import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

public class Display extends JPanel {
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame parentFrame;
	private ArrayList<PaintableObject> paintObjs;
	private MouseEvents mouseEventHandler;
	private boolean bCloseWindow;
	private DisplayMenuBar disMenuBar;
	private KeyEventHandler keyEventHandler;
	
	// Constants
	private final Color BG_COLOR = new Color(0.1f, 0.1f, 0.1f);
	
	/**
	 * Constructs the Display.
	 */
	public Display() {
		super();
		
		parentFrame = new JFrame("Display");
		bCloseWindow = false;
		
		paintObjs = new ArrayList<PaintableObject>();
		mouseEventHandler = new MouseEvents();
		keyEventHandler = new KeyEventHandler();
		
		disMenuBar = null;
	}
	
	/**
	 * Opens the GUI and adds a listener for closing it
	 */
	public void prepareGUI() {
		this.setSize(1400, 1000);
		this.addMouseListener(mouseEventHandler);
		this.addMouseWheelListener(mouseEventHandler);
		this.addMouseMotionListener(mouseEventHandler);
		parentFrame.addKeyListener(keyEventHandler);
		
		parentFrame.setSize(1400, 1000);
		parentFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				bCloseWindow = true;
			}
		});
		
		//new DisplayTextField(this);
		
		parentFrame.add(this);
		parentFrame.setVisible(true);
	}
	
	/**
	 * Override paintComponent to paint all the paintable objects
	 * 
	 * @param _graphics_
	 * 				The graphics to paint to.
	 */
	@Override
	protected void paintComponent(Graphics _graphics_) {
		super.paintComponent(_graphics_);
		_graphics_.setColor(BG_COLOR);
		_graphics_.fillRect(0, 0, getWidth(), getHeight());
		for (PaintableObject pObj : paintObjs) {
			pObj.paint(_graphics_);
		}
	}
	
	/**
	 * Should only be used by DisplayMenuBar to put itself 
	 * as the display menu bar for this display.
	 * 
	 * @param _menuBar_
	 * 				The new menu bar.
	 */
	public void addMenuBar(DisplayMenuBar _menuBar_) {
		parentFrame.setJMenuBar(_menuBar_);
		disMenuBar = _menuBar_;
	}
	
	/**
	 * Add a paintable object to this display.
	 * 
	 * @param _objToAdd_
	 * 				PaintableObject to add.
	 */
	public void addPaintableObj(PaintableObject _objToAdd_) { paintObjs.add(_objToAdd_); }
	
	/**
	 * Returns the mouseEventHandler.
	 * 
	 * @return MouseEvents
	 */
	public MouseEvents getMouseEvents() { return mouseEventHandler; }
	/**
	 * Returns if we should close the display.
	 * 
	 * @return boolean
	 */
	public boolean getShouldCloseWindow() { return bCloseWindow; }
	/**
	 * Returns the menu event listener.
	 * 
	 * @return MenuActionListener
	 */
	public MenuActionEventHandler getMenuEvents() { return disMenuBar.getMenuEventHandler(); }
	/**
	 * Returns the keyEventHandler.
	 * 
	 * @return KeyEventHandler
	 */
	public KeyEventHandler getKeyEvents() { return keyEventHandler; }
	/**
	 * Returns the JFrame of the display.
	 * 
	 * @return JFrame
	 */
	public JFrame getJFrame() { return parentFrame; }
	/**
	 * Returns the center of the display.
	 * 
	 * @return JFrame
	 */
	public Vector2Int getDisplayCenter() { return new Vector2Int(getWidth() / 2, getHeight() / 2); }
}
