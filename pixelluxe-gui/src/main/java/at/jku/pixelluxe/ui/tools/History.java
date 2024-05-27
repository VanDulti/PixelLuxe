package at.jku.pixelluxe.ui.tools;

import at.jku.pixelluxe.image.PaintableImage;

import java.util.List;
import java.util.Stack;

public class History {
	private int maxSize;
	private final Stack<PaintableImage> history;

	public History(int maxSize) {
		this.maxSize = maxSize;
		history = new Stack<>();
	}

	public PaintableImage rollBack() {
		if(history.size() > 0) {
			return history.pop();
		}
		return null;
	}

	public PaintableImage resume() {
		return null;
	}

	public boolean add(PaintableImage image) {
		if(history.size() < maxSize) {
			history.push(image);
			return true;
		}
		return false;
	}

}
