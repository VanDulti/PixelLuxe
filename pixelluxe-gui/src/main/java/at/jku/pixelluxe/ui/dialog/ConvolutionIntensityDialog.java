package at.jku.pixelluxe.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * A dialog for selecting the intensity of a convolution. The intensity is a value between 0 and 5.
 */
public class ConvolutionIntensityDialog {
	private final JDialog dialog;
	private int intensity;
	private JSlider slider;

	/**
	 * Creates a new ConvolutionIntensityDialog.
	 *
	 * @param frame  the parent frame
	 * @param width  the width of the dialog
	 * @param height the height of the dialog
	 */
	public ConvolutionIntensityDialog(JFrame frame, int width, int height) {
		this.dialog = new JDialog(frame, "Select Intensity", true);
		dialog.setSize(width, height);
		addComponents();
	}

	private void addComponents() {
		createDrawSlider(0, 5, 1);
		createDialogButton("Submit");
	}

	private void createDrawSlider(int min, int max, int startValue) {
		slider = new JSlider(JSlider.HORIZONTAL, min, max, startValue);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for (int i = slider.getMinimum(); i <= slider.getMaximum(); i++) {
			labelTable.put(i, new JLabel("" + i));
		}
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);

		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		dialog.add(slider, BorderLayout.NORTH);
	}

	private void createDialogButton(String label) {
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
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public int getConvIntensity() {
		return intensity;
	}
}