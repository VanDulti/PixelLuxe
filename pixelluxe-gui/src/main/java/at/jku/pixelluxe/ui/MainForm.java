package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.SimplePaintableImage;
import at.jku.pixelluxe.ui.dialog.DrawDialog;
import at.jku.pixelluxe.ui.tools.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;


public class MainForm extends JPanel {
	WorkingArea defaultTab = null;
	JToolBar mainToolBar = null;
	public void initialize() {


		setLayout(new BorderLayout(16, 16));

		JPanel toolbarPanel = new JPanel();

		toolbarPanel.setLayout(new BorderLayout());

		mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);

		JToggleButton  button = new JToggleButton("Draw");
		mainToolBar.add(button);
		button.addItemListener(this::drawButtonPressed);


		JToolBar supplementaryToolBar = new JToolBar();
		supplementaryToolBar.setFloatable(false);

		JButton anotherButton = new JButton("Click me too!");
		supplementaryToolBar.add(anotherButton);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);


		BufferedImage image = loadDemoImage();

		defaultTab = new WorkingArea(new SimplePaintableImage(image));
		defaultTab.initialize();


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

	private BufferedImage loadDemoImage() {
		try {
			URL res = getClass().getClassLoader().getResource("lenna.png");
			return ImageIO.read(Objects.requireNonNull(res));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void drawButtonPressed(ItemEvent e) {
		if(defaultTab == null || mainToolBar == null) {
			return;
		}


		DrawDialog drawDialog = new DrawDialog(Main.getMainFrame(), 600, 500);
		Color col = drawDialog.getBrushColor();
		int brushWidth = drawDialog.getBrushWidth();

		WorkingTool brush = new Brush(brushWidth, col);
		defaultTab.setTool(brush);


	}


	private Color setRGBAColor(int red, int green, int blue, double alpha) {
		int alphaInt = (int)(alpha*255);
		int argb = (alphaInt << 24) | (red << 16) | (green << 8) | blue;
		return new Color(argb);
	}



}
