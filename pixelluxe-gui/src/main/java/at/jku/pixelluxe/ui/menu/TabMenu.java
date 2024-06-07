package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * A menu that shall contain all tab-related actions (e.g. close tab, etc.).
 */
public class TabMenu extends JMenu implements Component<JMenu> {
	private final Runnable onClose;

	/**
	 * Creates a new TabMenu.
	 *
	 * @param onClose the action to perform when the close tab is selected
	 */
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
