package at.jku.pixelluxe.ui.tabs;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;

public class DefaultTab implements Component<JComponent> {
	@Override
	public JComponent initialize() {
		return new JLabel("Start by opening an image!", SwingConstants.CENTER);
	}
}
