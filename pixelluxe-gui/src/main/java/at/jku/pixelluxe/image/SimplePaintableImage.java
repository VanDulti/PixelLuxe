package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * As the name suggest, a simple implementation of a paintable image, based on a buffered image that is directly drawn
 * on.
 */
public class SimplePaintableImage implements PaintableImage {private BufferedImage image;

	private BufferedImage[] layers = new BufferedImage[2];

	/**
	 * Creates a new paintable image from the given image, using it as a base layer and painting everything on a virtual layer on top of it.
	 * This allows for erasing previously drawn lines for example.
	 * 
	 * @param image the backing buffered image
	 */
	public SimplePaintableImage(BufferedImage image) {
		this.image = image;
		layers[0] = cloneBufferedImage(image);
		layers[1] = createToolLayer(image);
	}

	private SimplePaintableImage(BufferedImage img, BufferedImage[] layers) {
		this(img);
		this.layers = layers;
	}
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
		Graphics2D graphics = (Graphics2D) layers[1].getGraphics();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(width));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(x1, y1, x2, y2);
		mergeLayers();
	}

	//Only a simple erasor;
	@Override
	public void eraseLine(int x1, int y1, int x2, int y2, int width) {
		Graphics2D graphics = (Graphics2D) layers[1].getGraphics();
		graphics.setComposite(AlphaComposite.Clear);
		graphics.setStroke(new BasicStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		graphics.drawLine(x1,y1, x2, y2);
		mergeLayers();
	}

	@Override
	public SimplePaintableImage copy() {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = this.image.copyData(null);
		BufferedImage[] newLayers = new BufferedImage[layers.length];
		for(int i = 0; i<newLayers.length;i++) {
			newLayers[i] = cloneBufferedImage(layers[i]);
		}
		return new SimplePaintableImage(new BufferedImage(cm,raster,isAlphaPremultiplied,null), newLayers);
	}


	private BufferedImage createToolLayer(BufferedImage img) {
		BufferedImage toolLayer = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = toolLayer.createGraphics();
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0,0,toolLayer.getWidth(), toolLayer.getHeight());
		g.dispose();
		return toolLayer;
	}

	private void mergeLayers() {
		BufferedImage rootImage = cloneBufferedImage(layers[0]);
		Graphics2D rootGraphic = rootImage.createGraphics();
		rootGraphic.drawImage(layers[1], 0, 0, null);
		rootGraphic.dispose();
		image = rootImage;
	}

	private BufferedImage cloneBufferedImage(BufferedImage img) {
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm,raster,isAlphaPremultiplied,null);
	}

}
