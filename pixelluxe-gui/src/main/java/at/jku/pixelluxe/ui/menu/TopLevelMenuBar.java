package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;

/**
 * A menu bar component intended for declarative creation of top-level menu bars. Just add the menus you want to the
 * constructor and call {@link #initialize()} to create the menu bar.
 */
public class TopLevelMenuBar extends JMenuBar implements Component<JMenuBar> {
	private final Component<JMenu>[] menus;

	/**
	 * @param menus the menus to add to the menu bar
	 */
	@SafeVarargs
	public TopLevelMenuBar(Component<JMenu>... menus) {
		this.menus = menus;
	}

	@Override
	public TopLevelMenuBar initialize() {
		for (Component<JMenu> menu : menus) {
			add(menu.initialize());
		}
		return this;
	}
}
