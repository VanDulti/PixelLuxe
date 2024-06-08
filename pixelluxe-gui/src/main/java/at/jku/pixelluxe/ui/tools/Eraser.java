package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.WorkingArea;
import at.jku.pixelluxe.ui.dialog.DrawDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Eraser implements WorkingTool {

	private final int width;


	public Eraser(int width) {
		this.width = width;
	}

	@Override
	public void drag(PaintableImage image, int x1, int y1, int x2, int y2) {
		image.eraseLine(x1, y1, x2, y2, width);
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

	public static class EraserActionListener implements ActionListener {

		private final JTabbedPane tabPane;

		public EraserActionListener(JTabbedPane tabPane) {
			this.tabPane = tabPane;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabPane.getSelectedIndex();
			Component c = tabPane.getComponentAt(selectedIndex);
			if (!(c instanceof WorkingArea workingArea)) {
				return;
			}

			DrawDialog drawDialog = new DrawDialog(null, 250, 100, 2, 26, 4, 2);
			int eraserWidth = drawDialog.getBrushWidth();
			Eraser eraser = new Eraser(eraserWidth);
			workingArea.setTool(eraser);
		}

	}
}
