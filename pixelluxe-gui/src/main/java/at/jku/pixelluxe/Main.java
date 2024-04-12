package at.jku.pixelluxe;

import at.jku.pixelluxe.menu.TopLevelMenuBar;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
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
