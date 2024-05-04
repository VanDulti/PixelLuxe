package at.jku.pixelluxe.image;

import java.io.File;
import java.util.Optional;

public record ImageFile(PaintableImage image, Optional<File> backingFile) {
}
