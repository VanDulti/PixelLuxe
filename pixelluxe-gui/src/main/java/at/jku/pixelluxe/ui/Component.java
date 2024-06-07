package at.jku.pixelluxe.ui;

import javax.swing.*;

/**
 * A generic component interface for nesting components in a more declarative way.
 *
 * @param <T> the type of the swing component to be initialized
 */
public interface Component<T extends JComponent> {
	/**
	 * Initializes the component and returns it. This is supposed to contain all possibly ui-related setup code. Must
	 * only be called once per instance. Otherwise, the component might lose state.
	 *
	 * @return the initialized component ready to be used inside other swing components
	 */
	T initialize();
}
