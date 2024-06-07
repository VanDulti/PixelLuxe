package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.ui.Component;
import at.jku.pixelluxe.ui.components.ImageHistogram;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Supplier;

/**
 * Menu for analyzing images, e.g. opening histograms.
 */
public class AnalyzeMenu extends JMenu implements Component<JMenu> {
	private final Supplier<ImageFile> onOpenHistogram;

	/**
	 * Creates a new AnalyzeMenu.
	 *
	 * @param onOpenHistogram the supplier for opening a histogram
	 */
	public AnalyzeMenu(Supplier<ImageFile> onOpenHistogram) {
		super("Analyze");
		this.onOpenHistogram = onOpenHistogram;
	}

	@Override
	public JMenu initialize() {
		JMenuItem closeTabButton = new JMenuItem("Open Histogram", KeyEvent.VK_W);
		closeTabButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		closeTabButton.addActionListener(this::openHistogram);

		add(closeTabButton);
		return this;
	}

	private void openHistogram(ActionEvent actionEvent) {
		ImageFile imageFile = onOpenHistogram.get();
		if (imageFile == null) {
			return;
		}
		PaintableImage paintableImage = imageFile.image();
		BufferedImage image = paintableImage.image();
		ImageHistogram imageHistogram = new ImageHistogram(image);
		JDialog dialog = new JDialog();
		String fileName = imageFile.backingFile().map(File::getName).orElse("[untitled]");
		dialog.setTitle("Histogram of %s".formatted(fileName));
		dialog.add(imageHistogram.initialize());
		dialog.setSize(600, 600);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}
}
