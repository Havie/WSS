import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

public class Display extends JPanel {
	private JFrame parentFrame;
	private ArrayList<PaintableObject> paintObjs;
	private MouseEvents mouseEventHandler;
	
	// Constants
	private final Color BG_COLOR = Color.BLACK;
	
	/**
	 * Constructs the Display.
	 */
	public Display() {
		super();
		
		parentFrame = new JFrame("Display");
		
		paintObjs = new ArrayList<PaintableObject>();
		mouseEventHandler = new MouseEvents();
		
		prepareGUI();
	}
	
	/**
	 * Opens the GUI and adds a listener for closing it
	 */
	public void prepareGUI() {
		this.setSize(1400, 1000);
		this.addMouseListener(mouseEventHandler);
		this.addMouseWheelListener(mouseEventHandler);
		this.addMouseMotionListener(mouseEventHandler);
		
		parentFrame.setSize(1400, 1000);
		parentFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
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
		_graphics_.setColor(Color.BLACK);
		_graphics_.fillRect(0, 0, getWidth(), getHeight());
		for (PaintableObject pObj : paintObjs) {
			pObj.paint(_graphics_);
		}
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
}
