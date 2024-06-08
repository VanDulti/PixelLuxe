package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.tools.History;
import at.jku.pixelluxe.ui.tools.RectangularSelectionTool;
import at.jku.pixelluxe.ui.tools.WorkingTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static at.jku.pixelluxe.Main.app;

/**
 * A working are is a container for a single {@link PaintableImage}, that allows manipulation of the image with tools,
 * as well as zooming and panning.
 */
public class WorkingArea extends JPanel {
	/**
	 * The minimum frames per second that the working area should be rendered with. This is used to prevent the working
	 * area from being rendered too often, which would be a waste of resources.
	 */
	public static final double MIN_FRAMES_PER_SECOND = 30.0;
	/**
	 * The minimum frame time in milliseconds that the working area should be rendered with. This is calculated from the
	 * {@link #MIN_FRAMES_PER_SECOND}.
	 */
	public static final long MIN_FRAME_TIME_MILLIS = (long) (1000.0 / MIN_FRAMES_PER_SECOND);
	/**
	 * The threshold for the scale at which the pixel grid should be drawn. If the scale is below this threshold, the
	 * pixel grid will not be drawn.
	 */
	public static final int PIXEL_GRID_SCALE_THRESHOLD = 20;
	/**
	 * The minimum amount of pixels of an image that should be visible on the screen. This is used to prevent the image
	 * from being panned completely out of view.
	 */
	private static final int MINIMAL_VISIBLE_SPACE = 10;
	/**
	 * The history object that keeps track of the changes made to the image.
	 */
	public History<PaintableImage> historyObj;
	/**
	 * The buffered image to be displayed. The actual content should then be drawn on-top of that BufferedImage (actual
	 * image, filters, layers, raw painting) as this is supposed to be more efficient than drawing directly on the
	 * JPanel.
	 */
	private PaintableImage image;
	/**
	 * The current location of the image's top left corner. Used as the current origin for any sort of translation of
	 * the image (panning).
	 */
	private double x = 0.0;
	/**
	 * The current location of the image's top left corner. Used as the current origin for any sort of translation of
	 * the image (panning).
	 */
	private double y = 0.0;
	/**
	 * The current scale of the image. Used to zoom in and out of the image.
	 */
	private double scale = 1.0;
	private BufferedImage selectedImage;
	private ToolListener toolListener;

	/**
	 * Creates a new working area with the given image.
	 *
	 * @param image the image to be displayed in the working area
	 */
	public WorkingArea(PaintableImage image) {
		this.image = image;
		setDoubleBuffered(true);
	}

	/**
	 * @return the image displayed in the working area
	 */
	public BufferedImage getImage() {
		return image.image();
	}

	/**
	 * Resets the image to the given image.
	 *
	 * @param image the image to be displayed in the working area
	 */
	public void setImage(PaintableImage image) {
		this.image = image;
		render();
	}

	/**
	 * Triggers the working area to repaint itself. This should be called whenever the image is changed or the working
	 * area needs to be updated.
	 */
	void render() {
		repaint(MIN_FRAME_TIME_MILLIS);
	}

	/**
	 * Initializes the working area by setting up the listeners and the initial state.
	 * <p>
	 * This method should be called after the working area has been added to a container and is visible.
	 */
	public void initialize() {
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		addListeners();
		historyObj = new History<>(image.copy(), 8);
	}

	/**
	 * Adds all sorts of listeners to the working area, mainly listeners for the tools and image panning/zooming.
	 */
	public void addListeners() {
		ImageDragListener imageDragListener = new ImageDragListener();
		toolListener = new ToolListener();

		addMouseMotionListener(imageDragListener);
		addMouseListener(imageDragListener);

		addMouseMotionListener(toolListener);
		addMouseListener(toolListener);

		addMouseWheelListener(new ImageScaleListener());
		addComponentListener(new WindowChangedListener());
	}

	/**
	 * Paints the backing paintable image onto the working area. This method is called whenever the working area needs
	 * to be repainted.
	 * <p>
	 * If the scale is above a certain threshold, a pixel grid will be drawn around the image's pixels. This is to
	 * provide a better visual representation of the image's pixels when zoomed in (in beta).
	 * <p>
	 * If a tool that needs visual representation that does not belong to the actual image, it will be drawn here as
	 * well.
	 * <p>
	 * <b>Attention:</b> Must NEVER be called directly, use {@link #render()} instead for managed repainting of the
	 * working area.
	 * <hr>
	 * {@inheritDoc}
	 */
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

		g2d.drawImage(image.image(), 0,0,this);

		g2d.setTransform(oldTransform);
		if (scale > PIXEL_GRID_SCALE_THRESHOLD) {
			// Drawing a pixel grid around the images pixels (if zoomed in)
			// kinda ugly tho
			drawPixelGrid(g2d);
		}

		if (toolListener.getCurrentTool() != null) {
			toolListener.getCurrentTool().draw(g2d);
		}
	}

	/**
	 * Adds rendering hints to the graphics object to make the pixel grid look better (essentially drawing the current
	 * image's pixels accurately, without any interpolation).
	 * <p>
	 * (In beta, still kinda ugly and flickery)
	 *
	 * @param g2d the graphics object to set the rendering hints on
	 */
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

	/**
	 * Little helper method to get the graphics object as a Graphics2D object.
	 *
	 * @return the graphics object as a Graphics2D object
	 */
	@Override
	public Graphics2D getGraphics() {
		return (Graphics2D) super.getGraphics();
	}

	/**
	 * Takes a snapshot of the current image and adds it to the history.
	 */
	public void takeSnapshot() {
		historyObj.add(image.copy());
		render();
	}

	/**
	 * Restricts the bounds of the image to prevent it from being panned out of view. Can be used anywhere to correct
	 * the bounds of the image.
	 */
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

	public WorkingTool getSelectedTool() {
		return toolListener.getCurrentTool();
	}

	/**
	 * Sets the tool that should be used for the tool actions.
	 *
	 * @param tool the tool to be used
	 */
	public void setTool(WorkingTool tool) {
		toolListener.setCurrentTool(tool);
	}

	/**
	 * Redoes the last action that was undone with {@link #undo()}. This will then update the app's backing model.
	 */
	public void redo() {
		PaintableImage newImage = historyObj.resume();
		image = newImage.copy();
		render();
		app.update(image);
	}

	/**
	 * Undoes the last action that was done to the image. This will then update the app's backing model.
	 */
	public void undo() {
		PaintableImage newImage = historyObj.rollBack();
		image = newImage.copy();
		render();
		app.update(image);
	}

	/**
	 * A listener that listens for changes in the window size and adjusts the image's bounds accordingly. This makes the
	 * image stay in view even when the window is resized.
	 */
	private class WindowChangedListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			restrictBounds();
			render();
		}
	}

	/*
		ToolListener executes Tools on a certain Mouse Event
		Tools are executed with tool.execute() for example Brush Tool
	 */

	/**
	 * A listener that listens for mouse events and executes the currently selected tool on the working area. All tool
	 * events are propagated relative to the working area's scale and position, as such, the tools do not need to worry
	 * about the scale and position of the working area.
	 */
	private class ToolListener extends MouseAdapter {
		private Point initialPoint = null;
		private WorkingTool currentTool = null;

		/**
		 * Processes mouse pressed events. If the current tool is not null, the tool will be executed on the working
		 * area.
		 * <hr>
		 * {@inheritDoc}
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (initialPoint != null || currentTool == null) {
				return;
			}

			initialPoint = e.getPoint();

			int x = getRelativeX(initialPoint);
			int y = getRelativeY(initialPoint);

			currentTool.set(image, x, y);

		}


		/**
		 * Processes mouse released events. If the current tool is not null, the tool will be executed on the working
		 * area.
		 * <hr>
		 * {@inheritDoc}
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			takeSnapshot();
			initialPoint = null;
			if (currentTool != null) {
				int x = getRelativeX(e.getPoint());
				int y = getRelativeY(e.getPoint());
				currentTool.release(image, x, y);
				render();

				if (currentTool instanceof RectangularSelectionTool selectionTool) {
					Rectangle selection = selectionTool.getSelection();
					if (selection != null) {
						selectedImage = image.image().getSubimage(selection.x, selection.y, selection.width, selection.height);
					}
				}
			}
		}

		/**
		 * Processes mouse dragged events. If the current tool is not null, the tool will be executed on the working
		 * area.
		 * <hr>
		 * {@inheritDoc}
		 */
		@Override
		public void mouseDragged(MouseEvent e) {

			if (initialPoint == null || currentTool == null) {
				return;
			}

			Point currentPoint = e.getPoint();

			int startPosX = getRelativeX(initialPoint);
			int startPosY = getRelativeY(initialPoint);

			int endPosX = getRelativeX(currentPoint);
			int endPosY = getRelativeY(currentPoint);

			currentTool.drag(image, startPosX, startPosY, endPosX, endPosY);

			initialPoint = currentPoint;
			render();
		}


		/**
		 * Calculates the relative x coordinate of a point in the working area (relative to the image's scale and
		 * position).
		 *
		 * @param point the point to calculate the relative x coordinate for
		 * @return the relative x coordinate of the point
		 */
		private int getRelativeX(Point point) {
			return (int) ((point.x - x) / scale);
		}

		/**
		 * Calculates the relative y coordinate of a point in the working area (relative to the image's scale and
		 * position).
		 *
		 * @param point the point to calculate the relative y coordinate for
		 * @return the relative y coordinate of the point
		 */
		private int getRelativeY(Point point) {
			return (int) ((point.y - y) / scale);
		}

		/**
		 * @return the current tool that is being used on the working area
		 */
		public WorkingTool getCurrentTool() {
			return currentTool;
		}

		/**
		 * Sets the current tool that should be used on the working area.
		 *
		 * @param tool the tool to be used
		 */
		public void setCurrentTool(WorkingTool tool) {
			currentTool = tool;
		}
	}

	/**
	 * A listener that listens for mouse events and allows the user to drag the image around the working area. Images
	 * can be dragged around by holding the control key and dragging the image with the mouse.
	 */
	private class ImageDragListener extends MouseAdapter {
		private Point initialPoint = null;

		/**
		 * Sets the initial point of the drag event.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			initialPoint = e.getPoint();
		}

		/**
		 * Voids the initial point of the drag event.
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			initialPoint = null;
		}

		/**
		 * Drags the image around the working area. The image can be dragged around by holding the control key and
		 * dragging the image with the mouse.
		 * <p>
		 * Ensures that the image is not dragged out of view.
		 */
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

		private static boolean isDragKeyDown(MouseEvent e) {
			return e.isControlDown();
		}
	}

	/**
	 * A listener that listens for mouse wheel events and allows the user to zoom in and out of the image. The image can
	 * be zoomed in and out by holding the alt key and scrolling the mouse wheel.
	 */
	private class ImageScaleListener extends MouseAdapter {
		private static final double MIN_SCALE = 0.1;
		private static final double MAX_SCALE = 30.0;


		/**
		 * Processes mouse wheel events and zooms in and out of the image. The image can be zoomed in and out by holding
		 * the alt key and scrolling the mouse wheel.
		 */
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