/* package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
public class FreehandSelectionTool implements WorkingTool {
	private ArrayList<Point> points;
	private boolean selecting;

	public FreehandSelectionTool() {
		points = new ArrayList<>();
		selecting = false;
	}

	@Override
	public void set(PaintableImage image, int x, int y) {
		points.clear();
		points.add(new Point(x, y));
		selecting = true;
	}

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public void drag(PaintableImage image, int x1, int y1, int x2, int y2) {
		if (selecting) {
			points.add(new Point(x2, y2));
		}
	}

	@Override
	public void release(PaintableImage image, int x, int y) {
		selecting = false;
	}

	@Override
	public void draw(Graphics2D g) {
		if (selecting && points.size() > 1) {
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(2));
			for (int i = 0; i < points.size() - 1; i++) {
				Point p1 = points.get(i);
				Point p2 = points.get(i + 1);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}
}



*/









