package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

import java.awt.*;

/**
 * The interface for tools that can be used in the working area.
 */
public interface WorkingTool {

	/**
	 * Tools that work with dragging should implement this method.
	 *
	 * @param image the image to work on
	 * @param x1    the x coordinate of the drag starting point
	 * @param y1    the y coordinate of the drag starting point
	 * @param x2    the x coordinate of the drag end point
	 * @param y2    the y coordinate of the drag end point
	 */
	void drag(PaintableImage image, int x1, int y1, int x2, int y2);

	/**
	 * Tools that work on a specific point should implement this method
	 *
	 * @param image the image to work on
	 * @param x1    the mouse x coordinate
	 * @param y1    the mouse y coordinate
	 */
	void set(PaintableImage image, int x1, int y1);

	void release(PaintableImage image, int x, int y);

	/**
	 * @param g
	 */
	void draw(Graphics2D g);
}
