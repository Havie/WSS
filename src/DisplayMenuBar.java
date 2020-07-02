import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class DisplayMenuBar extends JMenuBar {
	public DisplayMenuBar() {
		super();
		
		JMenu menu = new JMenu("Tools");
		menu.add(new JMenuItem("Search"));
		add(menu);
	}
}
