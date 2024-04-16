package at.jku.pixelluxe.ui.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class FileMenu extends JMenu {
	private final JFileChooser fc;

	public FileMenu() {
		super("File");
		this.fc = new JFileChooser();
	}

	public FileMenu initialize() {
		JMenuItem saveButton = new JMenuItem("Save");
		saveButton.addActionListener(this::saveButtonPressed);

		JMenuItem saveAsButton = new JMenuItem("Save as");
		saveAsButton.addActionListener(this::saveAsButtonPressed);

		JMenuItem openButton = new JMenuItem("Open");
		openButton.addActionListener(this::openButtonPressed);

		add(openButton);
		add(saveButton);
		add(saveAsButton);

		return this;
	}

	private void saveButtonPressed(ActionEvent actionEvent) {
		// TODO: Save the file or call saveAsButtonPressed if no backing file is present
	}

	public void saveAsButtonPressed(ActionEvent e) {
		try {
			fc.setDialogType(JFileChooser.SAVE_DIALOG);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.CANCEL_OPTION) {
				return;
			}
			if (returnVal == JFileChooser.ERROR_OPTION) {
				JOptionPane.showMessageDialog(this, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			File file = fc.getSelectedFile();
			if (file.isDirectory()) {
				JOptionPane.showMessageDialog(this, "You have to select a file, not a directory.", "Invalid file", JOptionPane.ERROR_MESSAGE);
				return;
			}
			boolean fileCreateResult = file.createNewFile();
			if (!fileCreateResult) {
				// File already exists, ask user if they want to overwrite it
				int result = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.NO_OPTION) {
					return;
				}
			}
			if (!file.canWrite()) {
				JOptionPane.showMessageDialog(this, "You don't have permission to write to this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
				saveAsButtonPressed(e);
				return;
			}
			// TODO: Save the file
		} catch (SecurityException exception) {
			JOptionPane.showMessageDialog(this, "You don't have permission to write to this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(this, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openButtonPressed(ActionEvent actionEvent) {
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return;
		}
		if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		File file = fc.getSelectedFile();
		if (file.isDirectory()) {
			JOptionPane.showMessageDialog(this, "You have to select a file, not a directory.", "Invalid file", JOptionPane.ERROR_MESSAGE);
			openButtonPressed(actionEvent);
			return;
		}
		if (!file.canRead()) {
			JOptionPane.showMessageDialog(this, "You don't have permission to read this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// TODO: Open the file
	}

}
