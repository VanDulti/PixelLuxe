package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.menu.ColorPane;
import at.jku.pixelluxe.ui.tabs.DefaultTab;
import at.jku.pixelluxe.ui.tools.Brush;
import at.jku.pixelluxe.ui.tools.ColorPicker;
import at.jku.pixelluxe.ui.tools.Eraser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.Component;
import java.io.File;
import java.util.function.Consumer;

public class Body extends JPanel {
	private final JTabbedPane tabPane;
	private ColorPicker colorPicker = null;
	private final Consumer<Integer> onTabSelectionChanged;
	private final DefaultTab defaultTab = new DefaultTab();

	public Body(Consumer<Integer> onTabSelectionChanged) {
		this.onTabSelectionChanged = onTabSelectionChanged;
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void initialize() {
		colorPicker = new ColorPicker();
		setLayout(new BorderLayout(16, 16));

		JPanel toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BorderLayout());

		JToolBar mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);

		JButton drawBtn = new JButton("Draw");
		mainToolBar.add(drawBtn);
		drawBtn.addActionListener(new Brush.BrushActionListener(tabPane, colorPicker));

		JButton eraseBtn = new JButton("Erase");
		mainToolBar.add(eraseBtn);
		eraseBtn.addActionListener(new Eraser.EraserActionListener(tabPane));

		ColorPane colorPane = new ColorPane();
		colorPane.addTo(mainToolBar);
		colorPane.getComponent().addActionListener(new ColorPicker.ColorPaneListener(
				tabPane,
				colorPicker,
				colorPane));

		JToolBar supplementaryToolBar = new JToolBar();
		supplementaryToolBar.setFloatable(false);

		JButton anotherButton = new JButton("Click me too!");
		supplementaryToolBar.add(anotherButton);

		tabPane.addChangeListener(this::selectionChanged);
		tabPane.addTab("Default tab", defaultTab.initialize());

		toolbarPanel.add(mainToolBar, BorderLayout.WEST);
		toolbarPanel.add(supplementaryToolBar, BorderLayout.EAST);

		add(toolbarPanel, BorderLayout.PAGE_START);
		add(tabPane, BorderLayout.CENTER);
	}

	private void selectionChanged(ChangeEvent changeEvent) {
		System.out.println(tabPane.getSelectedIndex());
		onTabSelectionChanged.accept(tabPane.getSelectedIndex() - 1);
	}

	public void addImage(ImageFile imageFile) {
		String title = imageFile.backingFile().map(File::getName).orElse("[Untitled]");
		WorkingArea workingArea = new WorkingArea(imageFile.image());
		int tabCount = tabPane.getTabCount();
		tabPane.insertTab(title, null, workingArea, title, tabCount);
		tabPane.setSelectedIndex(tabCount);
		workingArea.initialize();
	}

	public void updateImage(PaintableImage paintableImage, boolean takeSnapshot) {
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.setImage(paintableImage);
		repaint();
		if(takeSnapshot) {
			workingArea.takeSnapshot();
		}
	}


	public void removeActiveTab() {
		int selected = tabPane.getSelectedIndex();
		tabPane.removeTabAt(selected);
	}

	public JTabbedPane getTabPane() {
		return tabPane;
	}
}
