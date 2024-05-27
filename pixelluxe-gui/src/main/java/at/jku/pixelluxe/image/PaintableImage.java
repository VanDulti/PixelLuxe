package at.jku.pixelluxe.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Something to be painted inside {@link at.jku.pixelluxe.ui.WorkingArea}
 */
public interface PaintableImage {
	int getWidth();

	int getHeight();

	BufferedImage image();

	void drawLine(int x1, int y1, int x2, int y2, Color color, int width);

	void eraseLine(int x1, int y1,  int x2, int y2, int width);

	Color getColor(int x, int y);

	PaintableImage cloneImage();
}
