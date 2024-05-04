package at.jku.pixelluxe.ui;

import javax.swing.*;

public interface Component<T extends JComponent> {
	T initialize();
}
