package at.jku.pixelluxe.ui.components;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

/**
 * Displays the histogram of an image's color channels (red, green, blue, grayscale (combined rgb), alpha - depending on
 * their availability in the respective image).
 */
public class ImageHistogram implements Component<Box> {
	private final BufferedImage image;

	/**
	 * Creates a new ImageHistogram for the given image.
	 *
	 * @param image the image to display the histogram for
	 */
	public ImageHistogram(BufferedImage image) {
		this.image = image;
	}

	/**
	 * @return a centered header label for the histogram
	 */
	private static JLabel getHeader() {
		JLabel header = new JLabel("Histogram", JLabel.CENTER);
		header.setFont(header.getFont().deriveFont(16f));
		header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		return header;
	}

	/**
	 * Initializes the histogram.
	 * <p>
	 * Depending on the color space of the image, the histogram will be created for the color channels red, green, blue,
	 * grayscale (combined rgb) and alpha (if available).
	 *
	 * @return the initialized histogram
	 */
	@Override
	public Box initialize() {
		int type = image.getColorModel().getColorSpace().getType();
		return switch (type) {
			case ColorSpace.TYPE_RGB -> createRgbHistogram();
			case ColorSpace.TYPE_GRAY -> createGrayHistogram();
			default -> showUnsupportedColorSpace();
		};

	}

	/**
	 * @return the histogram for the color channels red, green, blue, grayscale (combined rgb) and alpha (if available).
	 */
	private Box createRgbHistogram() {
		Box box = Box.createVerticalBox();
		box.add(getHeader());
		box.add(getRedPanel());
		box.add(getGreenPanel());
		box.add(getBluePanel());
		box.add(getGrayScalePanel());
		if (image.getColorModel().hasAlpha()) {
			box.add(getAlphaPanel());
		}
		box.add(new JSeparator());
		return box;
	}

	/**
	 * @return the histogram for the grayscale (combined rgb) and alpha (if available).
	 */
	private Box createGrayHistogram() {
		Box box = Box.createVerticalBox();
		box.add(getHeader());
		box.add(getGrayScalePanel());
		if (image.getColorModel().hasAlpha()) {
			box.add(getAlphaPanel());
		}
		box.add(new JSeparator());
		return box;
	}

	/**
	 * @return a message that the color space is not supported.
	 */
	private Box showUnsupportedColorSpace() {
		Box box = Box.createVerticalBox();
		JLabel unsupported = new JLabel("Unsupported color space", JLabel.CENTER);
		unsupported.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		box.add(unsupported);
		return box;
	}

	/**
	 * @return a panel containing the histogram for the red color channel
	 */
	private Box getRedPanel() {
		Histogram histogram = Histogram.red(image);
		return getPanelForColorChannel("Red:", histogram);
	}

	/**
	 * @return a panel containing the histogram for the green color channel
	 */
	private Box getGreenPanel() {
		Histogram histogram = Histogram.green(image);
		return getPanelForColorChannel("Green:", histogram);
	}

	/**
	 * @return a panel containing the histogram for the blue color channel
	 */
	private Box getBluePanel() {
		Histogram histogram = Histogram.blue(image);
		return getPanelForColorChannel("Blue:", histogram);
	}

	/**
	 * @return a panel containing the histogram for the grayscale (combined rgb)
	 */
	private Box getGrayScalePanel() {
		Histogram histogram = Histogram.gray(image);
		return getPanelForColorChannel("Grayscale:", histogram);
	}

	/**
	 * @return a panel containing the histogram for the alpha channel
	 */
	private Box getAlphaPanel() {
		Histogram histogram = Histogram.alpha(image);
		return getPanelForColorChannel("Alpha:", histogram);
	}

	/**
	 * Creates a panel for a color channel with a label.
	 *
	 * @param label     the label for the color channel
	 * @param histogram the histogram for the color channel
	 * @return the panel containing the label and the histogram
	 */
	private Box getPanelForColorChannel(String label, Histogram histogram) {
		Box box = Box.createVerticalBox();

		JLabel colorLabel = new JLabel(label, JLabel.LEFT);
		colorLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		colorLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, colorLabel.getPreferredSize().height));
		colorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

		histogram.setBorder(BorderFactory.createDashedBorder(Color.lightGray, 1, 5, 5, true));

		box.add(colorLabel);
		box.add(histogram);
		return box;
	}
}
