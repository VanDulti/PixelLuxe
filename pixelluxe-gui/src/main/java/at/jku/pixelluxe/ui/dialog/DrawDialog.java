package at.jku.pixelluxe.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;


/**
 * Defines the Dialog which pops up when the Draw Button is pressed
 */

public class DrawDialog {
	private final JDialog dialog;
	private int brushWidth;
	private JSlider slider;



	public DrawDialog(JFrame frame, int width, int height, int min, int max, int thickSpacing, int startValue) {
		this.dialog = new JDialog(frame, "DrawDialog", true);
		dialog.setSize(width, height);
		addComponents(min,max, thickSpacing, startValue);
	}

	private void addComponents(int min, int max, int thickSpacing, int startValue) {
		createDrawSlider(min, max, thickSpacing, startValue);
		createDialogButton("Submit");
	}

	private void createDrawSlider(int min, int max, int thickSpacing, int startValue) {
		slider = new JSlider(JSlider.HORIZONTAL, min, max, startValue);
		//
//		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
////		for (int i = slider.getMinimum(); i <= slider.getMaximum(); i++) {
////			labelTable.put(i, new JLabel("" + i));
////		}
		slider.setMajorTickSpacing(thickSpacing);
		//slider.setLabelTable(labelTable);
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
					brushWidth = slider.getValue();
				}


				dialog.dispose();
			}
		});

		dialog.add(submitButton, BorderLayout.SOUTH);
		// Sets the position in the middle of the screen
		dialog.setLocationRelativeTo(null);

		dialog.setVisible(true);
	}

	public int getBrushWidth() {
		return brushWidth;
	}
}
