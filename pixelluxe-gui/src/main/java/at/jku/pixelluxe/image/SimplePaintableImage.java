package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * As the name suggest, a simple implementation of a paintable image, based on a buffered image that is directly drawn
 * on.
 *
 * @param image the backing buffered image
 */
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
	public BufferedImage image() {
		return image;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color color, int width) {
		Graphics2D graphics = graphics();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(width));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(x1, y1, x2, y2);
	}

	//Only a simple erasor;
	@Override
	public void eraseLine(int x1, int y1, int x2, int y2, int width) {
		Graphics2D graphics = graphics();
		graphics.setStroke(new BasicStroke(width));
		graphics.setColor(Color.WHITE);
		graphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public SimplePaintableImage copy() {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = this.image.copyData(null);
		return new SimplePaintableImage(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
	}

	private Graphics2D graphics() {
		return (Graphics2D) image.getGraphics();
	}
}
