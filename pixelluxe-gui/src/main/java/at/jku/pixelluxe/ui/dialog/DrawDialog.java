package at.jku.pixelluxe.ui.dialog;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


/*
	Defines the Dialog which pops up when the Draw Button is pressed
 */

public class DrawDialog {
	private int brushWidth;
	private Color brushCol;

	private JDialog dialog;

	private JSlider slider;

	private JColorChooser colorChooser;

	public DrawDialog(JFrame frame,int width, int height) {
		this.dialog = new JDialog(frame, "DrawDialog", true);
		dialog.setSize(width, height);
		addComponents();
	}

	private void addComponents() {
		createDrawSlider(1, 10, 1);
		createDrawColerChooser("RGB");
		createDrawSubmit("Submit");
	}

	private void createDrawSlider(int min, int max, int startValue) {
		slider = new JSlider(JSlider.HORIZONTAL, min, max, startValue);
		//
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for(int i = slider.getMinimum(); i<=slider.getMaximum(); i++) {
			labelTable.put(i, new JLabel("" + i));
		}
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(10);

		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		dialog.add(slider, BorderLayout.NORTH);
	}


	private void createDrawColerChooser(String chooserType) {
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

	private void createDrawSubmit(String label) {
		// Create a Submit Button
		JButton submitButton = new JButton(label);

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save changes here
				brushWidth = slider.getValue();
				brushCol = colorChooser.getColor();
				// Close the dialog
				dialog.dispose();
			}
		});
		dialog.add(submitButton, BorderLayout.PAGE_END);


		// Sets the position in the middle of the screen
		dialog.setLocationRelativeTo(null);

		dialog.setVisible(true);
	}

	public int getBrushWidth() {
		return brushWidth;
	}

	public Color getBrushColor() {
		return brushCol;
	}


}
