package at.jku.pixelluxe.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.IntUnaryOperator;

/**
 * A graphical representation of the distribution of pixel intensities in an image.
 */
public class Histogram extends JPanel {
	private final int[] histogram;
	private final Color color;

	/**
	 * Creates a new histogram for the given pixel intensity distribution.
	 *
	 * @param histogram the pixel intensity distribution
	 */
	public Histogram(int[] histogram) {
		this(histogram, null);
	}

	/**
	 * Creates a new histogram for the given pixel intensity distribution.
	 *
	 * @param histogram the pixel intensity distribution
	 * @param color     the color to use for the histogram bars
	 */
	public Histogram(int[] histogram, Color color) {
		this.histogram = histogram;
		this.color = color;
	}

	/**
	 * Creates a new histogram for the grayscale pixel intensity distribution of the given image.
	 *
	 * @param image the image to create the histogram for
	 * @return the histogram
	 */
	public static Histogram gray(BufferedImage image) {
		return ofImage(image, Color.lightGray, Histogram::extractGray);
	}

	/**
	 * Creates a new histogram for the pixel intensity distribution of the given image.
	 *
	 * @param image          the image to create the histogram for
	 * @param color          the color to use for the histogram bars
	 * @param valueExtractor the function to extract the pixel intensity from the image (e.g. red, green, blue, alpha)
	 * @return the histogram
	 */
	public static Histogram ofImage(BufferedImage image, Color color, IntUnaryOperator valueExtractor) {
		int[] histogram = calculateHistogram(image, valueExtractor);
		return new Histogram(histogram, color);
	}

	/**
	 * Extracts the grayscale value from the given RGB value.
	 *
	 * @param rgb the RGB value
	 * @return the grayscale value
	 */
	private static int extractGray(int rgb) {
		Color color = new Color(rgb);
		return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
	}

	/**
	 * Calculates the pixel intensity distribution of the given image.
	 *
	 * @param image          the image to calculate the histogram for
	 * @param valueExtractor the function to extract the pixel intensity from the image (e.g. red, green, blue, alpha)
	 * @return the pixel intensity distribution
	 */
	public static int[] calculateHistogram(BufferedImage image, IntUnaryOperator valueExtractor) {
		int[] histogram = new int[256]; // Assuming 8-bit grayscale image

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				int value = valueExtractor.applyAsInt(rgb);
				histogram[value]++;
			}
		}

		return histogram;
	}

	/**
	 * Creates a new histogram for the red pixel intensity distribution of the given image.
	 *
	 * @param image the image to create the histogram for
	 * @return the histogram
	 */
	public static Histogram red(BufferedImage image) {
		return ofImage(image, Color.red, Histogram::extractRed);
	}

	/**
	 * Extracts the red value from the given RGB value.
	 *
	 * @param rgb the RGB value
	 * @return the red value
	 */
	private static int extractRed(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	/**
	 * Creates a new histogram for the green pixel intensity distribution of the given image.
	 *
	 * @param image the image to create the histogram for
	 * @return the histogram
	 */
	public static Histogram green(BufferedImage image) {
		return ofImage(image, Color.green, Histogram::extractGreen);
	}

	/**
	 * Extracts the green value from the given RGB value.
	 *
	 * @param rgb the RGB value
	 * @return the green value
	 */
	private static int extractGreen(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	/**
	 * Creates a new histogram for the blue pixel intensity distribution of the given image.
	 *
	 * @param image the image to create the histogram for
	 * @return the histogram
	 */
	public static Histogram blue(BufferedImage image) {
		return ofImage(image, Color.blue, Histogram::extractBlue);
	}

	/**
	 * Extracts the blue value from the given RGB value.
	 *
	 * @param rgb the RGB value
	 * @return the blue value
	 */
	private static int extractBlue(int rgb) {
		return rgb & 0xFF;
	}

	/**
	 * Creates a new histogram for the alpha pixel intensity distribution of the given image.
	 *
	 * @param image the image to create the histogram for
	 * @return the histogram
	 */
	public static Histogram alpha(BufferedImage image) {
		return ofImage(image, Color.black, Histogram::extractAlpha);
	}

	/**
	 * Extracts the alpha value from the given RGB value.
	 *
	 * @param rgb the RGB value
	 * @return the alpha value
	 */
	private static int extractAlpha(int rgb) {
		return (rgb >> 24) & 0xFF;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawHistogram(g);
	}

	/**
	 * Draws the histogram.
	 *
	 * @param g the graphics context to draw on
	 */
	private void drawHistogram(Graphics g) {
		if (histogram == null) {
			return;
		}

		int width = getWidth();
		int height = getHeight();
		int maxCount = 0;

		for (int count : histogram) {
			if (count > maxCount) {
				maxCount = count;
			}
		}

		g.setColor(color);

		for (int i = 0; i < histogram.length; i++) {
			int value = histogram[i];
			int barWidth = (int) (((double) 1 / histogram.length) * width);
			int barHeight = (int) (((double) value / maxCount) * height);
			int x = (int) (((double) i / histogram.length) * width);
			int y = height - barHeight;
			g.fillRect(x, y, barWidth, barHeight);
		}
	}
}
