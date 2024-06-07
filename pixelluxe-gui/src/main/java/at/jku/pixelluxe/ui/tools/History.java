package at.jku.pixelluxe.ui.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to store the history of a certain object, in order to support undo and redo operations.
 *
 * @param <T> the type of the objects to store in the history
 */
public class History<T> {
	private final List<T> history;
	private int maxSize;
	private int pos = 0;

	/**
	 * Creates a new history with a maximum size. Be aware that specifying a large maximum size can lead to high memory
	 * usage.
	 *
	 * @param initialValue The initial value in the history.
	 * @param maxSize      The maximum number of elements to store in the history. If the history exceeds this size, the
	 *                     oldest element will be removed. Must be greater than 0.
	 */
	public History(T initialValue, int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("Max size must be greater than 0");
		}
		this.maxSize = maxSize;
		history = new ArrayList<>();
		history.add(initialValue);
	}

	/**
	 * Rolls back the history by one step and returns the previous element. If the history is at the beginning, the
	 * first element will be returned (you can't roll back further).
	 *
	 * @return the previous element in the history
	 */
	public T rollBack() {
		if (pos > 0) {
			pos = pos - 1;
			return history.get(pos);
		}
		return history.getFirst();
	}

	/**
	 * Moves the history one step forward and returns that next element. If the history is at the end, the last element
	 * will be returned (you can't step forward any further).
	 *
	 * @return the next element in the history
	 */
	public T resume() {
		if (pos < history.size() - 1) {
			pos++;
			return history.get(pos);
		}
		return history.getLast();
	}

	/**
	 * Appends a new element to the history.
	 *
	 * @param elem the element to add
	 */
	public void add(T elem) {
		if (pos < maxSize - 1) {
			pos++;
			if (history.size() < maxSize) {
				history.add(elem);
			} else {
				history.set(pos, elem);
			}
			return;
		}
		history.removeFirst();
		pos--;
		add(elem);
	}

}
