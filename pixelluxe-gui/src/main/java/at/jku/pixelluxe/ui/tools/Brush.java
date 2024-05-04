package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

import java.awt.*;

/*
	Brush class defines the properties of a brush
 */

public class Brush implements WorkingTool {

	private int width;
	private Color color;


	public Brush(int width, Color color) {
		this.color = color;
		this.width = width;
	}

	@Override
	public void execute(PaintableImage image, int x1, int y1, int x2, int y2) {
		image.drawLine(x1,y1,x2,y2,color,width);
	}

	@Override
	public String getToolName() {
		return getClass().getName();
	}


}
