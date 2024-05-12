package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.App;
import at.jku.pixelluxe.ui.WorkingArea;
import at.jku.pixelluxe.ui.dialog.ColorDialog;
import at.jku.pixelluxe.ui.menu.ColorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorPicker implements WorkingTool, Colorable {
	private Color color;


	public ColorPicker() {
		color = Color.WHITE;
	}

	@Override
	public void drag(PaintableImage image, int x1, int y1, int x2, int y2) {

	}

	@Override
	public void set(PaintableImage image, int x, int y) {
		color = image.getColor(x, y);
	}

	@Override
	public String getToolName() {
		return getClass().getName();
	}

	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	public static class ColorPaneListener implements ActionListener {

		private final JTabbedPane tabPane;

		private final ColorPicker colPicker;

		private final ColorPane colPane;

		public ColorPaneListener(JTabbedPane tabPane, ColorPicker colPicker, ColorPane colPane) {
			this.tabPane = tabPane;
			this.colPicker = colPicker;
			this.colPane = colPane;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabPane.getSelectedIndex();
			Component c = tabPane.getComponentAt(selectedIndex);
			if(!(c instanceof WorkingArea workingArea)) {
				return;
			}

			ColorDialog colorDialog = new ColorDialog(App.getMainFrame(), 600, 500);

			Color newColor = colorDialog.getColor();
			//Set Background Color
			JButton btn2 = colPane.getComponent();
			btn2.setBackground(newColor);

			colPicker.setColor(newColor);

			WorkingTool workingTool = workingArea.getTool();
			if(workingTool instanceof Colorable) {
				((Colorable) workingTool).setColor(newColor);
			}

		}
	}


}
