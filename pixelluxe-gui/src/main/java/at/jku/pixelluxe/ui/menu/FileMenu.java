package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class FileMenu extends JMenu implements Component<JMenu> {
	private final Runnable onOpen;
	private final Runnable onSave;
	private final Runnable onSaveAs;
	private final Runnable onUndo;
	private final Runnable onRedo;

	public FileMenu(Runnable onOpen, Runnable onSave, Runnable onSaveAs, Runnable onUndo, Runnable onRedo) {
		super("File");
		this.onOpen = onOpen;
		this.onSave = onSave;
		this.onSaveAs = onSaveAs;
		this.onUndo = onUndo;
		this.onRedo = onRedo;
	}

	@Override
	public JMenu initialize() {
		JMenuItem saveButton = new JMenuItem("Save", KeyEvent.VK_S);
		saveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveButton.addActionListener(action -> onSave.run());

		JMenuItem saveAsButton = new JMenuItem("Save as", KeyEvent.VK_SHIFT | KeyEvent.VK_S);
		saveAsButton.addActionListener(action -> onSaveAs.run());
		saveAsButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

		JMenuItem openButton = new JMenuItem("Open", KeyEvent.VK_O);
		openButton.addActionListener(action -> onOpen.run());
		openButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

		JMenuItem undoButton = new JMenuItem("Undo");
		undoButton.addActionListener(action -> onUndo.run());

		JMenuItem redoButton = new JMenuItem("Redo");
		redoButton.addActionListener(action -> onRedo.run());

		add(openButton);
		add(saveButton);
		add(saveAsButton);
		add(undoButton);
		add(redoButton);
		return this;
	}


}
