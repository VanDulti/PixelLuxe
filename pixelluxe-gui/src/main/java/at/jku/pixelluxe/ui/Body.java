package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.ui.dialog.DrawDialog;
import at.jku.pixelluxe.ui.tabs.DefaultTab;
import at.jku.pixelluxe.ui.tools.Brush;
import at.jku.pixelluxe.ui.tools.WorkingTool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.function.Consumer;

public class Body extends JPanel {
	private final JTabbedPane tabPane;
	private final Consumer<Integer> onTabSelectionChanged;
	private final DefaultTab defaultTab = new DefaultTab();

	public Body(Consumer<Integer> onTabSelectionChanged) {
		this.onTabSelectionChanged = onTabSelectionChanged;
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void initialize() {
		setLayout(new BorderLayout(16, 16));

		JPanel toolbarPanel = new JPanel();

		toolbarPanel.setLayout(new BorderLayout());

		JToolBar mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);

		JToggleButton  button = new JToggleButton("Draw");
		mainToolBar.add(button);
		button.addActionListener(this::drawButtonPressed);

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
		String title = imageFile.backingFile()
				.map(File::getName)
				.orElse("[Untitled]");
		WorkingArea workingArea = new WorkingArea(imageFile.image());
		int tabCount = tabPane.getTabCount();
		tabPane.insertTab(title, null, workingArea, title, tabCount);
		tabPane.setSelectedIndex(tabCount);
		workingArea.initialize();
	}

	public void removeActiveTab() {
		int selected = tabPane.getSelectedIndex();
		tabPane.removeTabAt(selected);
	}

	private void drawButtonPressed(ActionEvent e) {

		int selectedIndex = tabPane.getSelectedIndex();
        Component c = tabPane.getComponentAt(selectedIndex);
		if(!(c instanceof WorkingArea workingArea)) {
			return;
		}

		DrawDialog drawDialog = new DrawDialog(App.getMainFrame(), 600, 500);
		Color col = drawDialog.getBrushColor();
		int brushWidth = drawDialog.getBrushWidth();



		WorkingTool brush = new Brush(brushWidth, col);
		workingArea.setTool(brush);


	}


}
