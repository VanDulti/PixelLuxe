package at.jku.pixelluxe;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JPanel {
	public void initialize() {
		setLayout(new BorderLayout(16, 16));

		JPanel toolbarPanel = new JPanel();

		toolbarPanel.setLayout(new BorderLayout());

		JToolBar mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);

		JButton button = new JButton("Click me!");
		mainToolBar.add(button);

		JToolBar supplementaryToolBar = new JToolBar();
		supplementaryToolBar.setFloatable(false);

		JButton anotherButton = new JButton("Click me too!");
		supplementaryToolBar.add(anotherButton);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		JPanel defaultTab = new JPanel();
		defaultTab.setBackground(Color.LIGHT_GRAY);

		JPanel anotherTab = new JPanel();
		anotherTab.setBackground(Color.PINK);

		tabbedPane.addTab("Default tab", defaultTab);
		tabbedPane.addTab("Another tab", anotherTab);

		JLabel label = new JLabel("Hello World!");
		defaultTab.add(label);

		toolbarPanel.add(mainToolBar, BorderLayout.WEST);
		toolbarPanel.add(supplementaryToolBar, BorderLayout.EAST);

		add(toolbarPanel, BorderLayout.PAGE_START);
		add(tabbedPane, BorderLayout.CENTER);
	}
}
