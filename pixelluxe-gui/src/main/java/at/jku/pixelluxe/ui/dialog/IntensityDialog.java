package at.jku.pixelluxe.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;


/**
 * Defines the Dialog which pops up when the Draw Button is pressed
 */

public class IntensityDialog {
	private final JDialog dialog;
	private int intensity;
	private JSlider slider;


	public IntensityDialog(JFrame frame, int width, int height) {
		this.dialog = new JDialog(frame, "Select Intensity", true);
		dialog.setSize(width, height);
		addComponents();
	}

	private void addComponents() {
		createDrawSlider(-5, 5, 0);
		createDialogButton("Submit");
	}

	private void createDrawSlider(int min, int max, int startValue) {
		slider = new JSlider(JSlider.HORIZONTAL, min, max, startValue);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for (int i = slider.getMinimum(); i <= slider.getMaximum(); i++) {
			labelTable.put(i, new JLabel("" + i));
		}
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(10);

		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		dialog.add(slider, BorderLayout.NORTH);
	}

	private void createDialogButton(String label) {
		// Create a Submit Button
		JButton submitButton = new JButton(label);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (label.equals("Submit")) {
					// Save changes here
					intensity = slider.getValue();
				}


				dialog.dispose();
			}
		});

		dialog.add(submitButton, BorderLayout.SOUTH);
		// Sets the position in the middle of the screen
		dialog.setLocationRelativeTo(null);

		dialog.setVisible(true);
	}

	public int getIntensity() {
		return intensity * 2;
	}
}
