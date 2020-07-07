import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayPopup extends JFrame{
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DisplayPopup.
	 * Default.
	 */
	public DisplayPopup() {
		this("Popup", new Vector2Int(500, 309), new Vector2Int(0, 0), new Component[]{});
	}
	
	/**
	 * Constructs a DisplayPopup with a name, dimension, location, and child components.
	 * 
	 * @param _name_
	 * 				The name of the pop-up.
	 * @param _dim_
	 * 				The dimensions of the pop-up.
	 * @param _location_
	 * 				The location of the pop-up.
	 * @param _childComp_
	 * 				The children of the pop-up.
	 */
	public DisplayPopup(String _name_, Vector2Int _dim_,
			Vector2Int _location_, Component[] _childComp_) {
		super(_name_);
		
		JPanel panel = new JPanel();
		
		for (Component child : _childComp_) {
			if (child != null)
				panel.add(child);
		}
		
		this.add(panel);

		this.setSize(_dim_.getX(), _dim_.getY());
		this.setLocation(_location_.getX(), _location_.getY());
		this.setVisible(true);
	}
}
