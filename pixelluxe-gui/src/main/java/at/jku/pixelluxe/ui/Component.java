package at.jku.pixelluxe.ui;

import javax.swing.*;

/**
 * A generic component interface for nesting components in a more declarative way.
 *
 * @param <T> the type of the swing component to be initialized
 */
public interface Component<T extends JComponent> {
	T initialize();
}
