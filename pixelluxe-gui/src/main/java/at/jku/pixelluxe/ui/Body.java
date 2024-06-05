package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.menu.ColorPane;
import at.jku.pixelluxe.ui.tabs.DefaultTab;
import at.jku.pixelluxe.ui.tools.Brush;
import at.jku.pixelluxe.ui.tools.ColorPicker;
import at.jku.pixelluxe.ui.tools.Eraser;
import at.jku.pixelluxe.ui.tools.RectangularSelectionTool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.Component;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * The main content area of the application. Contains the tabs for the different images and the toolbar.
 */
public class Body extends JPanel {
	private final JTabbedPane tabPane;
	private final Consumer<Integer> onTabSelectionChanged;
	private final DefaultTab defaultTab = new DefaultTab();

	/**
	 * Creates a new body with the given tab selection change listener.
	 *
	 * @param onTabSelectionChanged the listener to be called when the selected tab changes
	 */
	public Body(Consumer<Integer> onTabSelectionChanged) {
		this.onTabSelectionChanged = onTabSelectionChanged;
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Initializes the body by setting up the toolbar and the default tab.
	 */
	public void initialize() {
		ColorPicker colorPicker = new ColorPicker();
		setLayout(new BorderLayout(16, 16));

		JPanel toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BorderLayout());

		JToolBar mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);

		JButton drawBtn = new JButton("Draw");
		mainToolBar.add(drawBtn);
		drawBtn.addActionListener(new Brush.BrushActionListener(tabPane, colorPicker));
		mainToolBar.setMargin(new Insets(0, 9, 0, 0));

		JButton eraseBtn = new JButton("Erase");
		mainToolBar.add(eraseBtn);
		eraseBtn.addActionListener(new Eraser.EraserActionListener(tabPane));

		JButton selectBtn = new JButton("Select");
		mainToolBar.add(selectBtn);
		selectBtn.addActionListener((e -> {
			int selectIndex = tabPane.getSelectedIndex();
			Component c = tabPane.getComponentAt(selectIndex);
			if (c instanceof WorkingArea workingArea) {
				workingArea.setTool(new RectangularSelectionTool());
			}
		}));

		ColorPane colorPane = new ColorPane();
		colorPane.addTo(mainToolBar);
		colorPane.getComponent().addActionListener(new ColorPicker.ColorPaneListener(
				tabPane,
				colorPicker,
				colorPane));

		JToolBar supplementaryToolBar = new JToolBar();
		supplementaryToolBar.setFloatable(false);

		tabPane.addChangeListener(this::selectionChanged);
		tabPane.addTab("Default tab", defaultTab.initialize());

		toolbarPanel.add(mainToolBar, BorderLayout.WEST);
		toolbarPanel.add(supplementaryToolBar, BorderLayout.EAST);

		add(toolbarPanel, BorderLayout.PAGE_START);
		add(tabPane, BorderLayout.CENTER);
	}

	/**
	 * Called when the selected tab changes.
	 */
	private void selectionChanged(ChangeEvent changeEvent) {
		System.out.println(tabPane.getSelectedIndex());
		onTabSelectionChanged.accept(tabPane.getSelectedIndex() - 1);
	}

	/**
	 * Adds a new image to the body. The image will be displayed in a new tab.
	 *
	 * @param imageFile the image file to be added
	 */
	public void addImage(ImageFile imageFile) {
		String title = imageFile.backingFile().map(File::getName).orElse("[Untitled]");
		WorkingArea workingArea = new WorkingArea(imageFile.image());
		int tabCount = tabPane.getTabCount();
		tabPane.insertTab(title, null, workingArea, title, tabCount);
		tabPane.setSelectedIndex(tabCount);
		workingArea.initialize();
	}

	/**
	 * Updates the image in the currently active tab.
	 *
	 * @param paintableImage the new image to be displayed
	 */
	public void updateImage(PaintableImage paintableImage) {
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.setImage(paintableImage);
		repaint();
		workingArea.takeSnapshot();
	}


	/**
	 * Removes the currently active tab.
	 */
	public void removeActiveTab() {
		int selected = tabPane.getSelectedIndex();
		tabPane.removeTabAt(selected);
	}

	/**
	 * @return the main tab pane that contains all the tabs
	 */
	public JTabbedPane getTabPane() {
		return tabPane;
	}
}
