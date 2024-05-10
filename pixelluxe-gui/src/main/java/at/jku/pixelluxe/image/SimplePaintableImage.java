package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public record SimplePaintableImage(BufferedImage image) implements PaintableImage {

	private static final Graphics2D prev = null;

	@Override
	public int getWidth() {
		return image.getWidth(null);
	}

	@Override
	public int getHeight() {
		return image.getHeight(null);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color color, int width) {
		Graphics2D graphics = graphics();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(width));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public Color getColor(int x, int y) {
		int rgba = image.getRGB(x, y);
		return new Color(rgba);
	}

	//Only a simple erasor;
	@Override
	public void eraseLine(int x1, int y1, int x2, int y2, int width) {
		Graphics2D graphics = graphics();
		graphics.setStroke(new BasicStroke(width));
		graphics.setColor(Color.WHITE);
		graphics.drawLine(x1, y1, x2, y2);
	}


	private Graphics2D graphics() {
		return (Graphics2D) image.getGraphics();
	}
}
