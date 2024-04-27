package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.filter.convolution.Convolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import at.jku.pixelluxe.filter.convolution.*;

public class MainForm extends JPanel {

	private WorkingArea defaultTab;

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

		JButton cB = new JButton("Convolution Filter");
		mainToolBar.add(cB);
		cB.addActionListener(e -> applyConvolutionFilter());

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);


		Image image = loadDemoImage();

		defaultTab = new WorkingArea(image);
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

	private void applyConvolutionFilter(){
		int w = loadDemoImage().getWidth(null);
		int h = loadDemoImage().getHeight(null);

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Convolution c = new Convolution();

		bi = c.filter(loadDemoImage(), Kernels.SHARPEN);
		defaultTab.setFilteredImage(bi);
	}

	private Image loadDemoImage() {
		try {
			URL res = getClass().getClassLoader().getResource("chamelo.jpg");
			return ImageIO.read(Objects.requireNonNull(res));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
