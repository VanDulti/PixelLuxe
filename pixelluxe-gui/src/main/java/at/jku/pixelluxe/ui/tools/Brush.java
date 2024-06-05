package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.App;
import at.jku.pixelluxe.ui.WorkingArea;
import at.jku.pixelluxe.ui.dialog.DrawDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
	Brush class defines the properties of a brush
 */

public class Brush implements WorkingTool, Colorable {

	private final int width;
	private Color color;

	public Brush(int width, Color color) {
		this.color = color;
		this.width = width;
	}


	@Override
	public void drag(PaintableImage image, int x1, int y1, int x2, int y2) {
		image.drawLine(x1, y1, x2, y2, color, width);
	}

	@Override
	public void set(PaintableImage image, int x1, int y1) {
	}

	@Override
	public void release(PaintableImage image, int x, int y) {

	}

	@Override
	public void draw(Graphics2D g) {

	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}


	public static class BrushActionListener implements ActionListener {

		private final JTabbedPane tabPane;
		private final ColorPicker colorPicker;

		public BrushActionListener(JTabbedPane tabPane, ColorPicker colorPicker) {
			this.tabPane = tabPane;
			this.colorPicker = colorPicker;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabPane.getSelectedIndex();
			Component c = tabPane.getComponentAt(selectedIndex);
			if (!(c instanceof WorkingArea workingArea)) {
				return;
			}

			DrawDialog drawDialog = new DrawDialog(null, 200, 150);
			int brushWidth = drawDialog.getBrushWidth();
			Brush brush = new Brush(brushWidth, colorPicker.getColor());
			workingArea.setTool(brush);
		}

	}

}
