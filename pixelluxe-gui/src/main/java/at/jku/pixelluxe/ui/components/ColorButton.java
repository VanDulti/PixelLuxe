package at.jku.pixelluxe.ui.components;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * A colored button that can be clicked to request a new color from a listener.
 */
public class ColorButton implements Component<JButton> {
	private final JButton button;

	public ColorButton() {
		button = new JButton("     ");
	}

	/**
	 * Adds a listener that is called whenever the button is clicked. The listener is supposed to return a new color or
	 * null if the color should not be changed.
	 *
	 * @param listener the supplier that provides the new color on button click (could be a supplier opening a color
	 *                 picker dialog)
	 */
	public void addListener(Supplier<Color> listener) {
		button.addActionListener(_ -> {
			Color color = listener.get();
			if (color != null) {
				button.setBackground(color);
			}
		});
	}

	@Override
	public JButton initialize() {
		button.setBackground(Color.WHITE);
		return button;
	}
}
