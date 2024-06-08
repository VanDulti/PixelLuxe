package at.jku.pixelluxe.image;

import java.io.File;
import java.util.Optional;

/**
 * A paintable image with an optional backing file.
 *
 * @param image       the paintable image
 * @param backingFile the backing file
 */
public record ImageFile(PaintableImage image, Optional<File> backingFile) {
}
