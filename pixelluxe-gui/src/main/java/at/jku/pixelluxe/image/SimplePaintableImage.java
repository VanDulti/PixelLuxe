package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public record SimplePaintableImage(Image image) implements PaintableImage {

	@Override
	public int getWidth() {
		return image.getWidth(null);
	}

	@Override
	public int getHeight() {
		return image.getHeight(null);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color color, int width)  {

		Graphics2D graphics = graphics();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(width));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(x1,y1,x2,y2);

	}


	private Graphics2D graphics() {
		return (Graphics2D) image.getGraphics();
	}
}
