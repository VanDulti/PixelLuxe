package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.WorkingArea;
import at.jku.pixelluxe.ui.components.ColorButton;
import at.jku.pixelluxe.ui.dialog.ColorDialog;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * A color picker tool that can be used to pick a color from the image.
 */
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

	}

	@Override
	public void release(PaintableImage image, int x, int y) {

	}

	@Override
	public void draw(Graphics2D g) {

	}

	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	public static class ColorPaneListener implements Supplier<Color> {

		private final JTabbedPane tabPane;

		private final ColorPicker colPicker;

		public ColorPaneListener(JTabbedPane tabPane, ColorPicker colPicker, ColorButton colPane) {
			this.tabPane = tabPane;
			this.colPicker = colPicker;
		}

		@Override
		public Color get() {
			int selectedIndex = tabPane.getSelectedIndex();
			Component c = tabPane.getComponentAt(selectedIndex);
			if (!(c instanceof WorkingArea workingArea)) {
				return null;
			}

			ColorDialog colorDialog = new ColorDialog(null, 600, 500);

			Color newColor = colorDialog.getColor();
			if (newColor == null) {
				return null;
			}
			colPicker.setColor(newColor);

			WorkingTool workingTool = workingArea.getSelectedTool();
			if (workingTool instanceof Colorable) {
				((Colorable) workingTool).setColor(newColor);
			}
			return newColor;
		}
	}


}
