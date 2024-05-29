package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.filter.Filter;
import at.jku.pixelluxe.filter.convolution.Convolution;
import at.jku.pixelluxe.filter.convolution.Kernels;
import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.Model;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.image.SimplePaintableImage;
import at.jku.pixelluxe.ui.dialog.IntesityDialog;
import at.jku.pixelluxe.ui.menu.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class App extends JPanel{
	private static final String[] READER_FILE_SUFFIXES = ImageIO.getReaderFileSuffixes();
	private static final String[] WRITER_FILE_SUFFIXES = ImageIO.getWriterFileSuffixes();
	private static final ExecutorService executorService = Executors.newWorkStealingPool();
	private static JFrame mainFrame;
	private final FileChooser fileChooser;
	private final AtomicReference<Model> model = new AtomicReference<>(new Model(List.of()));
	private int selectedImage = 0;
	private final Body body = new Body(
			this::tabSelectionChanged
	);


	private final TopLevelMenuBar menuBar = new TopLevelMenuBar(
			new FileMenu(
					this::onOpenPressed,
					this::onSavePressed,
					this::onSaveAsPressed,
					this::onUndoPressed,
					this::onRedoPressed
			),
			new TabMenu(
					this::onTabClose
			),
			new AnalyzeMenu(
					this::getSelectedImage
			),
			new FilterMenu(
					this::onInvertPressed,
					this::onContrastPressed,
					this::onSaturationPressed,
					this::onConvolutionPressed
			),
			new EdgeDetectionMenu(
					this::onXEdgePressed,
					this::onYEdgePressed,
					this::onXSobelPressed,
					this::onYSobelPressed,
					this::onLaplacePressed
			),
			new BlurSharpenMenu(
					this::onSharpenPressed,
					this::onGaussPressed,
					this::onMeanBlurPressed
			)

	);

	public App() {
		fileChooser = new FileChooser(
				body,
				new FileNameExtensionFilter("Images", READER_FILE_SUFFIXES),
				new FileNameExtensionFilter("Images", WRITER_FILE_SUFFIXES)
		);
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
		//mainFrame.setSize(mainFrame.getMaximumSize());
		mainFrame.setSize(new Dimension(1920, 1080));
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

	private void onUndoPressed() {
		JTabbedPane tabPane = body.getTabPane();
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.undo();
	}

	private void onRedoPressed() {
		JTabbedPane tabPane = body.getTabPane();
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.redo();
	}

	private void onConvolutionPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.ySobel);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	public void updateFromHistory(PaintableImage paintableImage) {
		this.model.updateAndGet(newmodel -> {
			int selectedImage = this.selectedImage;
			if (selectedImage < 0 || selectedImage >= newmodel.imageFiles().size()) {
				return newmodel;
			}
			return newmodel.with(selectedImage, new ImageFile(paintableImage, newmodel.imageFiles().get(selectedImage).backingFile()));
		});
	}

	private void updatePaintable(PaintableImage paintableImage, boolean takeSnapshot) {
		SwingUtilities.invokeLater(() -> body.updateImage(paintableImage, takeSnapshot));
		this.model.updateAndGet(newmodel -> {
			int selectedImage = this.selectedImage;
			if (selectedImage < 0 || selectedImage >= newmodel.imageFiles().size()) {
				return newmodel;
			}
			return newmodel.with(selectedImage, new ImageFile(paintableImage, newmodel.imageFiles().get(selectedImage).backingFile()));
		});
	}

	private void onSaturationPressed() {
		executorService.submit(() -> {
			int intensity = getIntensity();
			Model model = this.model.get();
			BufferedImage bi = new Filter().saturation(model.imageFiles().get(selectedImage).image().image(), intensity);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private int getIntensity() {
		return new IntesityDialog(App.getMainFrame(), 200, 150).getIntesity();
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}

	private void onContrastPressed() {
		executorService.submit(() -> {
			int intensity = getIntensity();
			Model model = this.model.get();
			BufferedImage bi = new Filter().contrast(model.imageFiles().get(selectedImage).image().image(), intensity);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onInvertPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Filter().invert(model.imageFiles().get(selectedImage).image().image());
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onXEdgePressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.xEdge);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onYEdgePressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.yEdge);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onXSobelPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.xSobel);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onYSobelPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.ySobel);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onLaplacePressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.laplace);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onSharpenPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.sharpen);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onGaussPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.gauss);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private void onMeanBlurPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedImage).image().image(), Kernels.meanBlur);
			updatePaintable(new SimplePaintableImage(bi), true);
		});
	}

	private ImageFile getSelectedImage() {
		int selected = this.selectedImage;
		Model model = this.model.get();
		List<ImageFile> imageFiles = model.imageFiles();
		if (selected < 0 || selected >= imageFiles.size()) {
			return null;
		}
		return imageFiles.get(selectedImage);
	}
}
