package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

import java.awt.*;

public class RectangularSelectionTool implements WorkingTool {
	private int startX, startY, endX, endY;
	private boolean selecting;
	private Rectangle selection;

	@Override
	public void set(PaintableImage image, int x, int y) {
		startX = x;
		startY = y;
		endX = x;
		endY = y;
		selecting = true;
		selection = null;
	}

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public void drag(PaintableImage image, int x1, int y1, int x2, int y2) {
		if (selecting) {
			endX = x2;
			endY = y2;
		}
	}

	@Override
	public void release(PaintableImage image, int x, int y) {
		selecting = false;
		selection = new Rectangle(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
	}

	@Override
	public void draw(Graphics2D g) {
		if (selecting) {
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(2));
			g.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
		}
	}

	public Rectangle getSelection() {
		return selection;
	}
}
