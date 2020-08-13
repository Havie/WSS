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
		JMenu fileMenu = new JMenu("File");
		JMenuItem buildItem = new JMenuItem("New Map");
		fileMenu.add(buildItem);
		//JMenuItem loadItem = new JMenuItem("Open");
		//fileMenu.add(loadItem);
		//JMenuItem saveItem = new JMenuItem("Save");
		//fileMenu.add(saveItem);
		
		JMenu toolsMenu = new JMenu("Tools");
		JMenuItem searchItem = new JMenuItem("Search");
		toolsMenu.add(searchItem);
		
		this.add(fileMenu);
		this.add(toolsMenu);
		
		menuEventHandler = new MenuActionEventHandler();
		
		buildItem.addActionListener(menuEventHandler);
		searchItem.addActionListener(menuEventHandler);
		//loadItem.addActionListener(menuEventHandler);
		//saveItem.addActionListener(menuEventHandler);
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