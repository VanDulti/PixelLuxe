package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;

public class TopLevelMenuBar extends JMenuBar implements Component<JMenuBar> {
	private final Component<JMenu>[] menus;

	@SafeVarargs
	public TopLevelMenuBar(Component<JMenu>... menus) {
		this.menus = menus;
	}

	public TopLevelMenuBar initialize() {
		for (Component<JMenu> menu : menus) {
			add(menu.initialize());
		}
		return this;
	}
}
