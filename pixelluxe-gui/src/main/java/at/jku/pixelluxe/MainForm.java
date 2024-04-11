package at.jku.pixelluxe;

import javax.swing.*;

public class MainForm {
	private JPanel mainPanel;

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void initialize() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JLabel label = new JLabel("Hello World!");
		mainPanel.add(label);
	}
}
