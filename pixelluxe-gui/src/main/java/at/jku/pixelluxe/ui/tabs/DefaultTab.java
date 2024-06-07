package at.jku.pixelluxe.ui.tabs;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;

/**
 * Default tab that is shown when no image is opened. In the future, this tab could contain some information or an
 * advanced welcome screen including recent files, etc.
 */
public class DefaultTab implements Component<JComponent> {
	@Override
	public JComponent initialize() {
		return new JLabel("Start by opening an image!", SwingConstants.CENTER);
	}
}
