package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.tools.History;
import at.jku.pixelluxe.ui.tools.WorkingTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static at.jku.pixelluxe.Main.app;

public class WorkingArea extends JPanel {

	public static final double MIN_FRAMES_PER_SECOND = 30.0;
	public static final long MIN_FRAME_TIME_MILLIS = (long) (1000.0 / MIN_FRAMES_PER_SECOND);
	private static final int MINIMAL_VISIBLE_SPACE = 10;
	private static final int PIXEL_GRID_SCALE_THRESHOLD = 20;
	/**
	 * The buffered image to be displayed. The actual content should then be drawn on-top of that BufferedImage (actual
	 * image, filters, layers, raw painting) as this is supposed to be more efficient than drawing directly on the
	 * JPanel.
	 */
	private PaintableImage image;
	private History<PaintableImage> historyObj;
	private double x = 0.0;
	private double y = 0.0;
	private double scale = 1.0;

	private ToolListener toolListener;

	public WorkingArea(PaintableImage image) {
		this.image = image;
		setDoubleBuffered(true);
	}

	private static void setPixelGridRenderingHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
	}

	public BufferedImage getImage() {
		return image.image();
	}

	public void setImage(PaintableImage image) {
		this.image =image;
		render();
	}


	public void initialize() {
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		addListeners();
		historyObj = new History(image.cloneImage(), 128);
	}

	public void addListeners() {
		ImageDragListener imageDragListener = new ImageDragListener();
		toolListener = new ToolListener();
		KeyListener keyListener = new KeyListener();

		addMouseMotionListener(imageDragListener);
		addMouseListener(imageDragListener);

		addMouseMotionListener(toolListener);
		addMouseListener(toolListener);

		addKeyListener(keyListener);
		requestFocusInWindow();

		addMouseWheelListener(new MouseWheelListener());
		addComponentListener(new ComponentListener());
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
		g2d.drawImage(image.image(), 0, 0, this);
		g2d.setTransform(oldTransform);
		if (scale > PIXEL_GRID_SCALE_THRESHOLD) {
			// Drawing a pixel grid around the images pixels (if zoomed in)
			// kinda ugly tho
			drawPixelGrid(g2d);
		}
	}

	public void takeSnapshot() {
		historyObj.add(image.cloneImage());
		render();
	}

	/**
	 * Draws a pixel grid around the image's pixels. Looks kinda ugly tho.
	 * TODO: Fix pixel accuracy
	 *
	 * @param g2d the graphics object to draw on
	 */
	private void drawPixelGrid(Graphics2D g2d) {
		g2d.setColor(Color.MAGENTA);
		int scaledWidth = (int) (image.getWidth() * scale);
		int scaledHeight = (int) (image.getHeight() * scale);
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
		int imageHeight = (int) (image.getHeight() * scale);
		int imageWidth = (int) (image.getWidth() * scale);
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

	public WorkingTool getTool() {
		return toolListener.getTool();
	}

	/**
		Is the Mehtod for other Classes primarly Working Area to set the Tools  the user selected
		Which will then be executed on mouse Events
	 **/
	public void setTool(WorkingTool tool) {
		toolListener.setTool(tool);
	}

	private class KeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.isControlDown() && e.getKeyCode() == 90) {
				PaintableImage newImage = historyObj.rollBack();

				image = newImage.cloneImage();
				render();
				app.update(image);
			}

			if(e.isControlDown()  && e.getKeyCode() == 89) {
				PaintableImage newImage = historyObj.resume();
				image = newImage.cloneImage();
				render();
				app.update(image);
			}
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

	/*
		ToolListener executes Tools on a certain Mouse Event
		Tools are executed with tool.execute() for example Brush Tool
	 */
	private class ToolListener extends MouseAdapter {
		private Point initialPoint = null;

		//All tools that will be executed are saved here
		private WorkingTool tool = null;

		@Override
		public void mousePressed(MouseEvent e) {
			if (initialPoint != null || tool == null) {
				return;
			}

			initialPoint = e.getPoint();

			int x = getRelativeX(initialPoint);
			int y = getRelativeY(initialPoint);

			tool.set(image, x, y);

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initialPoint = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {

			if (initialPoint == null || tool == null) {
				return;
			}

			Point currentPoint = e.getPoint();

			int startPosX = getRelativeX(initialPoint);
			int startPosY = getRelativeY(initialPoint);

			int endPosX = getRelativeX(currentPoint);
			int endPosY = getRelativeY(currentPoint);

			tool.drag(image, startPosX, startPosY, endPosX, endPosY);

			initialPoint = currentPoint;
			takeSnapshot();
			render();
		}

		private int getRelativeX(Point point) {
			return (int) ((point.x - x) / scale);
		}

		private int getRelativeY(Point point) {
			return (int) ((point.y - y) / scale);
		}

		public WorkingTool getTool() {
			return tool;
		}

		public void setTool(WorkingTool toolee) {
			tool = toolee;
		}

		private boolean isPaintKeyDown(MouseEvent e) {
			return e.isAltDown();
		}
	}

	private class ImageDragListener extends MouseAdapter {
		private Point initialPoint = null;

		private static boolean isDragKeyDown(MouseEvent e) {
			return e.isControlDown();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			initialPoint = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initialPoint = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
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
			new Rectangle();

			render();
		}
	}

	private class MouseWheelListener extends MouseAdapter {
		private static final double MIN_SCALE = 0.1;
		private static final double MAX_SCALE = 30.0;

		private static boolean isZoomKeyDown(MouseWheelEvent e) {
			return e.isAltDown();
		}

		private static boolean isHorizontalTranslationKeyDown(MouseWheelEvent e) {
			return e.isShiftDown();
		}

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
	}

}
