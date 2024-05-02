package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public record SimplePaintableImage(BufferedImage image) implements PaintableImage {

	@Override
	public int getWidth() {
		return image.getWidth(null);
	}

	@Override
	public int getHeight() {
		return image.getHeight(null);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		Graphics2D graphics = graphics();
		graphics.setColor(color);
		graphics.drawLine(x1, y1, x2, y2);
	}

	private Graphics2D graphics() {
		return (Graphics2D) image.getGraphics();
	}
}
