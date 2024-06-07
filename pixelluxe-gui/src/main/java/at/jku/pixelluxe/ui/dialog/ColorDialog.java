package at.jku.pixelluxe.ui.dialog;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A dialog for selecting a color.
 */
public class ColorDialog {
	private final JDialog dialog;
	private Color col;
	private JColorChooser colorChooser;

	/**
	 * Creates a new ColorDialog.
	 *
	 * @param frame  the parent frame
	 * @param width  the width of the dialog
	 * @param height the height of the dialog
	 */
	public ColorDialog(JFrame frame, int width, int height) {
		this.dialog = new JDialog(frame, "ColorDialog", true);
		dialog.setSize(width, height);
		addComponents();
	}

	private void addComponents() {
		createColerChooser("RGB");
		createSubmit("Submit");
	}

	private void createColerChooser(String chooserType) {
		// Create a JColorChooser
		colorChooser = new JColorChooser();
		AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();

		for (AbstractColorChooserPanel panel : panels) {
			// Check if this is not the RGB panel
			if (!panel.getDisplayName().equals(chooserType)) {
				// Remove the panel
				colorChooser.removeChooserPanel(panel);
			}
		}

		dialog.add(colorChooser, BorderLayout.CENTER);
	}

	private void createSubmit(String label) {
		// Create a Submit Button
		JButton submitButton = new JButton(label);

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save changes here
				col = colorChooser.getColor();
				// Close the dialog
				dialog.dispose();
			}
		});
		dialog.add(submitButton, BorderLayout.PAGE_END);


		// Sets the position in the middle of the screen
		dialog.setLocationRelativeTo(null);

		dialog.setVisible(true);
	}

	/**
	 * @return the selected color or null, if no color was selected
	 */
	public Color getColor() {
		return col;
	}

}
