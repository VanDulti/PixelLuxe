package at.jku.pixelluxe.image;

import java.util.ArrayList;
import java.util.List;

/**
 * This application's main model consisting of a list of image files including stateless operations on them.
 *
 * @param imageFiles the list of image files
 */
public record Model(List<ImageFile> imageFiles) {
	/**
	 * Adds an image file to the model and returns the new model.
	 *
	 * @param imageFile the image file to add
	 * @return the new model including the added image file at the end
	 */
	public Model add(ImageFile imageFile) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.add(imageFile);
		return new Model(List.copyOf(newImageFiles));
	}

	/**
	 * Replaces the image file at the given index with the given image file and returns the new model.
	 *
	 * @param index     the index of the image file to replace
	 * @param imageFile the image file to replace the old one with
	 * @return the new model including the replaced image file
	 */
	public Model with(int index, ImageFile imageFile) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.set(index, imageFile);
		return new Model(newImageFiles);
	}

	/**
	 * Removes the image file at the given index and returns the new model.
	 *
	 * @param index the index of the image file to remove
	 * @return the new model without the removed image file
	 */
	public Model without(int index) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.remove(index);
		return new Model(newImageFiles);
	}
}
