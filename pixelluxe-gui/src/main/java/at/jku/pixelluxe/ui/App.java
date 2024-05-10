package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.Model;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.image.SimplePaintableImage;
import at.jku.pixelluxe.ui.menu.FileChooser;
import at.jku.pixelluxe.ui.menu.FileMenu;
import at.jku.pixelluxe.ui.menu.TabMenu;
import at.jku.pixelluxe.ui.menu.TopLevelMenuBar;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class App {
	private static final String[] READER_FILE_SUFFIXES = ImageIO.getReaderFileSuffixes();
	private static final String[] WRITER_FILE_SUFFIXES = ImageIO.getWriterFileSuffixes();
	private final FileChooser fileChooser;
	private final AtomicReference<Model> model = new AtomicReference<>(new Model(List.of()));
	private int selectedImage = 0;

	private static JFrame mainFrame;

	private final Body body = new Body(
			this::tabSelectionChanged
	);
	private final TopLevelMenuBar menuBar = new TopLevelMenuBar(
			new FileMenu(
					this::onOpenPressed,
					this::onSavePressed,
					this::onSaveAsPressed
			),
			new TabMenu(this::onTabClose)
	);

	public App() {
		fileChooser = new FileChooser(
				body,
				new FileNameExtensionFilter("Images", READER_FILE_SUFFIXES),
				new FileNameExtensionFilter("Images", WRITER_FILE_SUFFIXES)
		);
	}

	public static void main(String[] args) {
		FlatDarkLaf.setup();
		new App().run();
	}

	public void run() {

		SwingUtilities.invokeLater(this::initialize);

	}

	private void initialize() {
		menuBar.initialize();
		body.initialize();
		mainFrame = new JFrame("PixelLuxe");
		mainFrame.setJMenuBar(menuBar);
		mainFrame.setContentPane(body);
		mainFrame.setResizable(true);
		mainFrame.setSize(mainFrame.getMaximumSize());
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addSampleImage();
	}

	private void addSampleImage() {
		try {
			URL lenna = Objects.requireNonNull(getClass().getClassLoader().getResource("lenna.png"));
			BufferedImage image = ImageIO.read(lenna);
			PaintableImage paintableImage = new SimplePaintableImage(image);
			ImageFile imageFile = new ImageFile(paintableImage, Optional.empty());
			model.updateAndGet(model -> model.add(imageFile));
			body.addImage(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void tabSelectionChanged(int selected) {
		this.selectedImage = selected;
	}

	private void onTabClose() {
		model.updateAndGet(model -> {
			int selectedImage = this.selectedImage;
			if (selectedImage < 0 || selectedImage >= model.imageFiles().size()) {
				return model;
			}
			body.removeActiveTab();
			return model.without(selectedImage);
		});
	}

	private void onOpenPressed() {
		Optional<File> file = fileChooser.showOpenDialog();
		if (file.isEmpty()) {
			return;
		}
		try {
			BufferedImage image = ImageIO.read(file.get());
			PaintableImage paintableImage = new SimplePaintableImage(image);
			ImageFile imageFile = new ImageFile(paintableImage, file);
			model.updateAndGet(model -> model.add(imageFile));
			body.addImage(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void onSavePressed() {
		int selected = this.selectedImage;
		Model model = this.model.get();
		if (selected < 0 || selected >= model.imageFiles().size()) {
			onSaveAsPressed();
			return;
		}
		ImageFile imageFile = model.imageFiles().get(selected);
		if (imageFile.backingFile().isEmpty()) {
			onSaveAsPressed();
			return;
		}
		File file = imageFile.backingFile().get();
		if (!file.exists()) {
			onSaveAsPressed();
			return;
		}
		try {
			ImageIO.write(imageFile.image().image(), "png", file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void onSaveAsPressed() {
		if (selectedImage < 0) {
			return;
		}
		Optional<File> file = fileChooser.showSaveDialog();
		if (file.isEmpty()) {
			return;
		}
		Model model = this.model.get();
		if (selectedImage < 0 || selectedImage >= model.imageFiles().size()) {
			return;
		}
		ImageFile imageFile = model.imageFiles().get(selectedImage);
		if (imageFile.backingFile().isEmpty()) {
			imageFile = new ImageFile(imageFile.image(), file);
		}
		try {
			ImageIO.write(imageFile.image().image(), "png", file.get());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}
}
