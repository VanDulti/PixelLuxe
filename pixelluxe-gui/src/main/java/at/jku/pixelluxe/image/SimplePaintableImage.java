package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class SimplePaintableImage implements PaintableImage {
	private AtomicReference<BufferedImage> image;

	public SimplePaintableImage(BufferedImage image) {
		this.image = new AtomicReference<>(image);
	}

	@Override
	public int getWidth() {
		return image().getWidth(null);
	}

	@Override
	public int getHeight() {
		return image().getHeight(null);
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
		int rgba = image().getRGB(x, y);
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
		return (Graphics2D) image().getGraphics();
	}

	@Override
	public SimplePaintableImage cloneImage() {
		ColorModel cm = image().getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = this.image().copyData(null);
		return new SimplePaintableImage(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
	}

	@Override
	public BufferedImage image() {
		return image.get();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (SimplePaintableImage) obj;
		return Objects.equals(this.image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(image);
	}

	@Override
	public String toString() {
		return "SimplePaintableImage[" +
				"image=" + image + ']';
	}

}
