package at.jku.pixelluxe.image;

import at.jku.pixelluxe.ui.WorkingArea;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Something to be painted inside a {@link WorkingArea}, usually an image. This interface provides methods for basic
 * image manipulation.
 */
public interface PaintableImage extends Cloneable {
	/**
	 * @return the width of the image in pixels, anyone using this image must be able to work with this width and expect
	 * the image to be this wide
	 */
	int getWidth();

	/**
	 * @return the height of the image in pixels, anyone using this image must be able to work with this height and
	 * expect the image to be this high
	 */
	int getHeight();

	/**
	 * @return the backing java buffered image of this paintable image. This image should be used for drawing
	 * operations. This is precisely what will be drawn on a {@link WorkingArea}.
	 */
	BufferedImage image();

	/**
	 * Draws a line on the image. It is not specified whether this method should use interpolation or not. The line
	 * should be drawn from starting point (x1, y1) to ending point (x2, y2) with the given color and width.
	 *
	 * @param x1    the x-coordinate of the starting point
	 * @param y1    the y-coordinate of the starting point
	 * @param x2    the x-coordinate of the ending point
	 * @param y2    the y-coordinate of the ending point
	 * @param color the color of the line
	 * @param width the width of the line
	 */
	void drawLine(int x1, int y1, int x2, int y2, Color color, int width);

	/**
	 * Erases a line on the image. It is not specified whether this method should use interpolation or not. The line
	 * should be erased from starting point (x1, y1) to ending point (x2, y2) with the given width.
	 *
	 * @param x1    the x-coordinate of the starting point
	 * @param y1    the y-coordinate of the starting point
	 * @param x2    the x-coordinate of the ending point
	 * @param y2    the y-coordinate of the ending point
	 * @param width the width of the line
	 */
	void eraseLine(int x1, int y1, int x2, int y2, int width);

	/**
	 * @return a copy of this image. The copy must be independent of the original image, i.e. changes to the copy should
	 * not affect the original image and vice versa.
	 */
	SimplePaintableImage copy();
}
