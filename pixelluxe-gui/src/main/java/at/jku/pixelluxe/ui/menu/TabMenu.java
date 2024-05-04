package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class TabMenu extends JMenu implements Component<JMenu> {
	private final Runnable onClose;

	public TabMenu(Runnable onClose) {
		super("Tab");
		this.onClose = onClose;
	}

	@Override
	public JMenu initialize() {
		JMenuItem closeTabButton = new JMenuItem("Close active tab", KeyEvent.VK_W);
		closeTabButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		closeTabButton.addActionListener(this::closeTab);

		add(closeTabButton);
		return this;
	}

	private void closeTab(ActionEvent actionEvent) {
		onClose.run();
	}
}
