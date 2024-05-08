package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

/*
	Working Tool is the interface for every Tool for the working area
*/

public interface WorkingTool {

	void drag(PaintableImage image, int x1, int y1, int x2, int y2);

	void set(PaintableImage image, int x1, int y1);

	String getToolName();

}
