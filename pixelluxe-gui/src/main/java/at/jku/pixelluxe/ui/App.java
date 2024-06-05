package at.jku.pixelluxe.ui;

import at.jku.pixelluxe.filter.Filter;
import at.jku.pixelluxe.filter.convolution.Convolution;
import at.jku.pixelluxe.filter.convolution.Kernel;
import at.jku.pixelluxe.filter.convolution.Kernels;
import at.jku.pixelluxe.image.ImageFile;
import at.jku.pixelluxe.image.Model;
import at.jku.pixelluxe.image.PaintableImage;
import at.jku.pixelluxe.image.SimplePaintableImage;
import at.jku.pixelluxe.ui.dialog.ConvIntensityDialog;
import at.jku.pixelluxe.ui.dialog.IntensityDialog;
import at.jku.pixelluxe.ui.menu.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.awt.*;
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

/**
 * The main application class for the PixelLuxe Gui.
 */
public class App extends JPanel {
	private static final String[] READER_FILE_SUFFIXES = ImageIO.getReaderFileSuffixes();
	private static final String[] WRITER_FILE_SUFFIXES = ImageIO.getWriterFileSuffixes();
	private static final ExecutorService executorService = Executors.newWorkStealingPool();
	private static JFrame mainFrame;
	private final FileChooser fileChooser;
	private final AtomicReference<Model> model = new AtomicReference<>(new Model(List.of()));
	private int selectedTab = 0;
	private final Body body = new Body(
			this::setSelectedTab
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
					this::getSelectedTab
			),
			new FilterMenu(
					this::onInvertPressed,
					this::onContrastPressed,
					this::onSaturationPressed,
					this::onGrayScalePressed
			),
			new ContourMenu(
					this::onHorizontalPressed,
					this::onVerticalPressed,
					this::onLaplacePressed,
					this::onOutlinePressed
			),
			new DetailMenu(
					this::onSharpenPressed,
					this::onEmbossPressed,
					this::onGaussPressed,
					this::onMeanBlurPressed
			),
			new SobelMenu(
					this::onTopSobelPressed,
					this::onBottomSobelPressed,
					this::onLeftSobelPressed,
					this::onRightSobelPressed
			)

	);

	/**
	 * Creates a new instance of the application.
	 * <p>
	 * This will not start the actual application. Call {@link #run()} for that.
	 */
	public App() {
		fileChooser = new FileChooser(
				body,
				new FileNameExtensionFilter("Images", READER_FILE_SUFFIXES),
				new FileNameExtensionFilter("Images", WRITER_FILE_SUFFIXES)
		);
	}

	/**
	 * Initializes the application.
	 */
	public void run() {
		SwingUtilities.invokeLater(this::initialize);
	}

	/**
	 * Creates the application main frame and initializes all components.
	 */
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

	/**
	 * Adds a sample image to model/view.
	 */
	private void addSampleImage() {
		try {
			URL imageUrl = Objects.requireNonNull(getClass().getClassLoader().getResource("maisonCarree.jpg"));
			BufferedImage image = ImageIO.read(imageUrl);
			PaintableImage paintableImage = new SimplePaintableImage(image);
			ImageFile imageFile = new ImageFile(paintableImage, Optional.empty());
			model.updateAndGet(model -> model.add(imageFile));
			body.addImage(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the currently selected tab from the model.
	 */
	private void onTabClose() {
		model.updateAndGet(model -> {
			int selectedImage = this.selectedTab;
			if (selectedImage < 0 || selectedImage >= model.imageFiles().size()) {
				return model;
			}
			body.removeActiveTab();
			return model.without(selectedImage);
		});
	}

	/**
	 * Opens a file dialog to select an image file to open. The selected image will be shown as a new tab.
	 */
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

	/**
	 * Saves the currently selected image to its backing file or opens a file dialog to select a file to save to.
	 */
	private void onSavePressed() {
		int selected = this.selectedTab;
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

	/**
	 * Opens a file dialog to select a (new) file to save the currently selected image to.
	 */
	private void onSaveAsPressed() {
		if (selectedTab < 0) {
			return;
		}
		Optional<File> file = fileChooser.showSaveDialog();
		if (file.isEmpty()) {
			return;
		}
		Model model = this.model.get();
		if (selectedTab < 0 || selectedTab >= model.imageFiles().size()) {
			return;
		}
		ImageFile imageFile = model.imageFiles().get(selectedTab);
		if (imageFile.backingFile().isEmpty()) {
			imageFile = new ImageFile(imageFile.image(), file);
		}
		try {
			ImageIO.write(imageFile.image().image(), "png", file.get());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Undoes the most recent action on the currently selected image.
	 */
	private void onUndoPressed() {
		JTabbedPane tabPane = body.getTabPane();
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.undo();
	}

	/**
	 * Redoes the most recent action on the currently selected image.
	 */
	private void onRedoPressed() {
		JTabbedPane tabPane = body.getTabPane();
		int selectedIndex = tabPane.getSelectedIndex();
		Component c = tabPane.getComponentAt(selectedIndex);
		if (!(c instanceof WorkingArea workingArea)) {
			return;
		}
		workingArea.redo();
	}

	/**
	 * Applies a saturation filter to the currently selected image with a user-defined intensity.
	 */
	private void onSaturationPressed() {
		executorService.submit(() -> {
			int intensity = getIntensity();
			Model model = this.model.get();
			BufferedImage bi = new Filter().saturation(model.imageFiles().get(selectedTab).image().image(), intensity);
			updateAndRepaint(new SimplePaintableImage(bi));
		});
	}

	/**
	 * Spawns a new dialog to get the intensity of a filter from the user.
	 *
	 * @return the intensity the user selected
	 */
	private int getIntensity() {
		return new IntensityDialog(null, 200, 150).getIntensity();
	}

	/**
	 * Replaces the current paintable image with the given one and asynchronously repaints the body.
	 *
	 * @param paintableImage the new paintable image
	 */
	public void updateAndRepaint(PaintableImage paintableImage) {
		SwingUtilities.invokeLater(() -> body.updateImage(paintableImage));
		update(paintableImage);
	}

	/**
	 * Replaces the currently opened paintable image with the given one by updating the model.
	 *
	 * @param paintableImage the new paintable image
	 */
	public void update(PaintableImage paintableImage) {
		this.model.updateAndGet(newmodel -> {
			int selectedImage = this.selectedTab;
			if (selectedImage < 0 || selectedImage >= newmodel.imageFiles().size()) {
				return newmodel;
			}
			return newmodel.with(selectedImage, new ImageFile(paintableImage, newmodel.imageFiles().get(selectedImage).backingFile()));
		});
	}

	/**
	 * Called when the contrast filter button is pressed.
	 */
	private void onContrastPressed() {
		executorService.submit(() -> {
			int intensity = getIntensity();
			Model model = this.model.get();
			BufferedImage bi = new Filter().contrast(model.imageFiles().get(selectedTab).image().image(), intensity);
			updateAndRepaint(new SimplePaintableImage(bi));
		});
	}

	/**
	 * Called when the invert filter button is pressed.
	 */
	private void onInvertPressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Filter().invert(model.imageFiles().get(selectedTab).image().image());
			updateAndRepaint(new SimplePaintableImage(bi));
		});
	}

	/**
	 * Called when the grayscale filter button is pressed.
	 */
	private void onGrayScalePressed() {
		executorService.submit(() -> {
			Model model = this.model.get();
			BufferedImage bi = new Filter().grayScale(model.imageFiles().get(selectedTab).image().image());
			updateAndRepaint(new SimplePaintableImage(bi));
		});
	}

	/**
	 * Called when the horizontal filter button is pressed.
	 */
	private void onHorizontalPressed() {
		applyKernel(Kernels.horizontal);
	}

	/**
	 * Applies the given convolution filter kernel to the currently selected image with a user-defined intensity.
	 *
	 * @param kernel the kernel to apply
	 */
	private void applyKernel(Kernel kernel) {
		executorService.submit(() -> {
			Model model = this.model.get();
			int intensity = getConvIntensity();
			BufferedImage bi = new Convolution().filter(model.imageFiles().get(selectedTab).image().image(), kernel, intensity);
			updateAndRepaint(new SimplePaintableImage(bi));
		});
	}

	/**
	 * Spawns a new dialog to get the intensity of a convolution filter from the user.
	 *
	 * @return the intensity the user selected
	 */
	private int getConvIntensity() {
		return new ConvIntensityDialog(null, 200, 110).getConvIntensity();
	}

	/**
	 * Called when the vertical filter button is pressed.
	 */
	private void onVerticalPressed() {
		applyKernel(Kernels.vertical);
	}

	/**
	 * Called when the laplace filter button is pressed.
	 */
	private void onLaplacePressed() {
		applyKernel(Kernels.laplace);
	}

	/**
	 * Called when the outline filter button is pressed.
	 */
	private void onOutlinePressed() {
		applyKernel(Kernels.outline);
	}

	/**
	 * Called when the sharpen filter button is pressed.
	 */
	private void onSharpenPressed() {
		applyKernel(Kernels.sharpen);
	}

	/**
	 * Called when the emboss filter button is pressed.
	 */
	private void onEmbossPressed() {
		applyKernel(Kernels.emboss);
	}

	/**
	 * Called when the gauss filter button is pressed.
	 */
	private void onGaussPressed() {
		applyKernel(Kernels.gauss);
	}

	/**
	 * Called when the mean blur filter button is pressed.
	 */
	private void onMeanBlurPressed() {
		applyKernel(Kernels.meanBlur);
	}

	/**
	 * Called when the top sobel filter button is pressed.
	 */
	private void onTopSobelPressed() {
		applyKernel(Kernels.topSobel);
	}

	/**
	 * Called when the bottom sobel filter button is pressed.
	 */
	private void onBottomSobelPressed() {
		applyKernel(Kernels.bottomSobel);
	}

	/**
	 * Called when the left sobel filter button is pressed.
	 */
	private void onLeftSobelPressed() {
		applyKernel(Kernels.leftSobel);
	}

	/**
	 * Called when the right sobel filter button is pressed.
	 */
	private void onRightSobelPressed() {
		applyKernel(Kernels.rightSobel);
	}

	/**
	 * Returns the backing ImageFile object for the currently selected tab.
	 *
	 * @return the currently selected tab or null the selected tab is out of bounds
	 */
	private ImageFile getSelectedTab() {
		int selected = this.selectedTab;
		Model model = this.model.get();
		List<ImageFile> imageFiles = model.imageFiles();
		if (selected < 0 || selected >= imageFiles.size()) {
			return null;
		}
		return imageFiles.get(selectedTab);
	}


	/**
	 * Updates the currently selected tab.
	 *
	 * @param selected the newly selected tab
	 */
	private void setSelectedTab(int selected) {
		this.selectedTab = selected;
	}
}