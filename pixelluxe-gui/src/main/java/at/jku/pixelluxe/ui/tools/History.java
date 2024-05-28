package at.jku.pixelluxe.ui.tools;
import java.util.ArrayList;
import java.util.List;

public class History<E> {

	private int maxSize;
	private final List<E> history;
	private int pos = 0;

	public History(E firstElem, int maxSize) {
		this.maxSize = maxSize;
		history = new ArrayList<>();
		history.add(firstElem);
	}

	public synchronized E rollBack() {
		if(pos > 0) {
			pos=pos-1;

			return history.get(pos);
		}
		return history.getFirst();
	}

	public synchronized E resume() {
		if(pos >= -1 && pos < history.size()-1) {
			pos++;
			return history.get(pos);
		}

		return history.getLast();
	}

	public synchronized boolean add(E elem) {
		if(history.size() < maxSize) {
			history.add(elem);
			pos++;
			return true;
		}
		return false;
	}

}
