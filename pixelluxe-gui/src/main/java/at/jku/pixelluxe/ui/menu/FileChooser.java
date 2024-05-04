package at.jku.pixelluxe.ui.menu;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileChooser {
	private final FileFilter openFileFilter;
	private final FileFilter saveFileFilter;
	private final JFileChooser fc;
	private final java.awt.Component parent;

	public FileChooser(java.awt.Component parent, FileFilter openFileFilter, FileFilter saveFileFilter) {
		this.openFileFilter = openFileFilter;
		this.saveFileFilter = saveFileFilter;
		this.fc = new JFileChooser();
		this.parent = parent;
	}

	public Optional<File> showSaveDialog() {
		try {
			fc.setDialogType(JFileChooser.SAVE_DIALOG);
			fc.setFileFilter(saveFileFilter);
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.CANCEL_OPTION) {
				return Optional.empty();
			}
			if (returnVal == JFileChooser.ERROR_OPTION) {
				JOptionPane.showMessageDialog(parent, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
				return Optional.empty();
			}
			File file = fc.getSelectedFile();
			if (file == null) {
				JOptionPane.showMessageDialog(parent, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
				return Optional.empty();
			}
			if (file.isDirectory()) {
				JOptionPane.showMessageDialog(parent, "You have to select a file, not a directory.", "Invalid file", JOptionPane.ERROR_MESSAGE);
				return showSaveDialog();
			}
			boolean fileCreateResult = file.createNewFile();
			if (!fileCreateResult) {
				int result = JOptionPane.showConfirmDialog(parent, "File already exists. Do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.NO_OPTION) {
					return showSaveDialog();
				}
			}
			if (!file.canWrite()) {
				JOptionPane.showMessageDialog(parent, "You don't have permission to write to this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
				return showSaveDialog();
			}
			return Optional.of(file);
		} catch (SecurityException exception) {
			JOptionPane.showMessageDialog(parent, "You don't have permission to write to this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
			return Optional.empty();
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(parent, "An error occurred while creating the file.", "Error", JOptionPane.ERROR_MESSAGE);
			return Optional.empty();
		}
	}

	public Optional<File> showOpenDialog() {
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setFileFilter(openFileFilter);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return Optional.empty();
		}
		if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(parent, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
			return Optional.empty();
		}
		File file = fc.getSelectedFile();
		if (file == null) {
			JOptionPane.showMessageDialog(parent, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
			return Optional.empty();
		}
		if (file.isDirectory()) {
			JOptionPane.showMessageDialog(parent, "You have to select a file, not a directory.", "Invalid file", JOptionPane.ERROR_MESSAGE);
			return showOpenDialog();
		}
		if (!file.canRead()) {
			JOptionPane.showMessageDialog(parent, "You don't have permission to read this file.", "Permission denied", JOptionPane.ERROR_MESSAGE);
			return showOpenDialog();
		}
		return Optional.of(file);
	}
}