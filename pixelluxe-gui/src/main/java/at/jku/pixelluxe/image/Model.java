package at.jku.pixelluxe.image;

import java.util.ArrayList;
import java.util.List;

public record Model(List<ImageFile> imageFiles) {
	public Model add(ImageFile imageFile) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.add(imageFile);
		return new Model(List.copyOf(newImageFiles));
	}

	public Model with(int index, ImageFile imageFile) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.set(index, imageFile);
		return new Model(newImageFiles);
	}

	public Model without(int index) {
		List<ImageFile> newImageFiles = new ArrayList<>(imageFiles);
		newImageFiles.remove(index);
		return new Model(newImageFiles);
	}
}
