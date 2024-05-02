package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.ui.menu.TopLevelMenuBar;

import com.formdev.flatlaf.FlatDarkLaf;


import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		SwingUtilities.invokeLater(this::initialize);
	}

	private void initialize() {

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println(e.getStackTrace());
		}

		MainForm mainForm = new MainForm();
		mainForm.initialize();


		JFrame mainFrame = new JFrame("PixelLuxe");
		mainFrame.setJMenuBar(new TopLevelMenuBar().initialize());
		mainFrame.setContentPane(mainForm);
		mainFrame.setResizable(true);
		mainFrame.setSize(mainFrame.getMaximumSize());
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		System.out.println("I hacked the program!");
	}
}
