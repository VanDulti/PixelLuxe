package at.jku.pixelluxe.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class WorkingArea extends JPanel {
	public static final double MIN_FRAMES_PER_SECOND = 30.0;
	public static final long MIN_FRAME_TIME_MILLIS = (long) (1000.0 / MIN_FRAMES_PER_SECOND);
	private static final int MINIMAL_VISIBLE_SPACE = 10;
	private static final int PIXEL_GRID_SCALE_THRESHOLD = 20;
	private double x = 0.0;
	private double y = 0.0;
	private double scale = 1.0;
	/**
	 * The buffered image to be displayed. The actual content should then be drawn on-top of that BufferedImage (actual
	 * image, filters, layers, raw painting) as this is supposed to be more efficient than drawing directly on the
	 * JPanel.
	 */
	private final Image image;

	public WorkingArea(Image image) {
		this.image = image;
	}

	public void initialize() {
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		addListeners();
	}

	public void addListeners() {
		ImageDragListener imageDragListener = new ImageDragListener();
		addMouseMotionListener(imageDragListener);
		addMouseListener(imageDragListener);
		addMouseWheelListener(new MouseWheelListener());
		addComponentListener(new ComponentListener());
		addKeyListener(new KeyListener());
	}

	void render() {
		repaint(MIN_FRAME_TIME_MILLIS);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (scale > PIXEL_GRID_SCALE_THRESHOLD) {
			// should make the pixel grid more pixel accurate, but it's still kinda ugly and flickery
			setPixelGridRenderingHints(g2d);
		}
		g2d.translate(x, y);
		AffineTransform oldTransform = g2d.getTransform();
		g2d.scale(scale, scale);
		g2d.drawImage(image, 0, 0, this);
		g2d.setTransform(oldTransform);
		if (scale > PIXEL_GRID_SCALE_THRESHOLD) {
			// Drawing a pixel grid around the images pixels (if zoomed in)
			// kinda ugly tho
			drawPixelGrid(g2d);
		}
	}

	private static void setPixelGridRenderingHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
	}

	/**
	 * Draws a pixel grid around the image's pixels. Looks kinda ugly tho.
	 * TODO: Fix pixel accuracy
	 *
	 * @param g2d the graphics object to draw on
	 */
	private void drawPixelGrid(Graphics2D g2d) {
		g2d.setColor(Color.MAGENTA);
		int scaledWidth = (int) (image.getWidth(null) * scale);
		int scaledHeight = (int) (image.getHeight(null) * scale);
		for (int i = 0; i < scaledWidth; i++) {
			int x = (int) (i * scale);
			g2d.drawLine(x, 0, x, scaledHeight);
		}
		for (int j = 0; j < scaledHeight; j++) {
			int y = (int) (j * scale);
			g2d.drawLine(0, y, scaledWidth, y);
		}
	}

	@Override
	public Graphics2D getGraphics() {
		return (Graphics2D) super.getGraphics();
	}

	private void restrictBounds() {
		int imageHeight = (int) (image.getHeight(null) * scale);
		int imageWidth = (int) (image.getWidth(null) * scale);
		if (x < -imageWidth + MINIMAL_VISIBLE_SPACE) {
			x = -imageWidth + MINIMAL_VISIBLE_SPACE;
		}
		if (x > getWidth() - MINIMAL_VISIBLE_SPACE) {
			x = getWidth() - MINIMAL_VISIBLE_SPACE;
		}
		if (y < -imageHeight + MINIMAL_VISIBLE_SPACE) {
			y = -imageHeight + MINIMAL_VISIBLE_SPACE;
		}
		if (y > getHeight() - MINIMAL_VISIBLE_SPACE) {
			y = getHeight() - MINIMAL_VISIBLE_SPACE;
		}
	}

	private static class KeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("Key released!");
		}
	}

	private class ComponentListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			restrictBounds();
			render();
			System.out.println("Component resized!");
		}
	}

	private class ImageDragListener extends MouseAdapter {
		private Point initialPoint = null;

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("Mouse pressed!");
			initialPoint = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("Mouse released!");
			initialPoint = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("Mouse dragged!");
			if (initialPoint == null) {
				return;
			}
			if (!isDragKeyDown(e)) {
				return;
			}
			Point currentPoint = e.getPoint();
			x += currentPoint.x - initialPoint.x;
			y += currentPoint.y - initialPoint.y;
			restrictBounds();

			initialPoint = currentPoint;
			render();
		}

		private static boolean isDragKeyDown(MouseEvent e) {
			return e.isControlDown();
		}


	}

	private class MouseWheelListener extends MouseAdapter {
		private static final double MIN_SCALE = 0.1;
		private static final double MAX_SCALE = 30.0;

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			e.consume();
			if (isZoomKeyDown(e)) {
				scale -= e.getWheelRotation() * 0.1;
				if (scale < MIN_SCALE) {
					scale = MIN_SCALE;
				}
				if (scale > MAX_SCALE) {
					scale = MAX_SCALE;
				}
			} else if (isHorizontalTranslationKeyDown(e)) {
				x -= e.getWheelRotation();
			} else {
				y -= e.getWheelRotation();
			}
			restrictBounds();
			render();
			System.out.println("Mouse wheel moved!");
		}

		private static boolean isZoomKeyDown(MouseWheelEvent e) {
			return e.isAltDown();
		}

		private static boolean isHorizontalTranslationKeyDown(MouseWheelEvent e) {
			return e.isShiftDown();
		}
	}
}
