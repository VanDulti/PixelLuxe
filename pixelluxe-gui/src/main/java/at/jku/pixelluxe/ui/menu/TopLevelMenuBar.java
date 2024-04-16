package at.jku.pixelluxe.ui.menu;

import javax.swing.*;

public class TopLevelMenuBar extends JMenuBar {
	FileMenu fileMenu = new FileMenu();
	public TopLevelMenuBar initialize() {
		add(fileMenu.initialize());
		return this;
	}
}
