package at.jku.pixelluxe.image;

import java.awt.*;

/**
 * Something to be painted inside {@link at.jku.pixelluxe.ui.WorkingArea}
 */
public interface PaintableImage {
	int getWidth();

	int getHeight();

	Image image();

	void drawLine(int x1, int y1, int x2, int y2, Color color);
}
