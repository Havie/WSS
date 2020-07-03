import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class DisplayMenuBar extends JMenuBar {
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	private MenuActionEventHandler menuEventHandler;	// The event handler for menu actions.
	
	/**
	 * Constructs a DisplayMenuBar.
	 * 
	 * @param _display_
	 * 				The display the menu bar will be on.
	 */
	public DisplayMenuBar(Display _display_) {
		super();
		
		// Make these into functions that take strings
		JMenu menu = new JMenu("Tools");
		JMenuItem mItem = new JMenuItem("Search");
		menu.add(mItem);
		add(menu);
		
		menuEventHandler = new MenuActionEventHandler();
		
		mItem.addActionListener(menuEventHandler);
		// End MAke these...
		
		_display_.addMenuBar(this);
	}
	
	/**
	 * Returns the menu event handler associated with this menu.
	 * 
	 * @return MenuActionListener
	 */
	public MenuActionEventHandler getMenuEventHandler() { return menuEventHandler; }
}