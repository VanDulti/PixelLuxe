package at.jku.pixelluxe.filter;

import java.awt.image.BufferedImage;

/**
 * Filter class for image manipulation. Offers lots of different filters.
 */
public class Filter {
	/**
	 * Computes a manipulated version of the given image with saturation updated to the given intensity.
	 *
	 * @param image     the image to manipulate
	 * @param intensity the intensity of the saturation change
	 * @return the manipulated image
	 */
	public BufferedImage saturation(BufferedImage image, int intensity) {
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int rawImage = image.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;
				float[] hsb = java.awt.Color.RGBtoHSB(red, green, blue, null);
				hsb[1] *= (float) (1 + 0.1 * intensity);
				hsb[1] = Math.max(0, Math.min(hsb[1], 1));
				image.setRGB(j, i, (alpha << 24) | (java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0x00FFFFFF));
			}
		}
		return image;
	}

	/**
	 * Computes a manipulated version of the given image with contrast updated to the given intensity.
	 *
	 * @param image     the image to manipulate
	 * @param intensity the intensity of the contrast change
	 */
	public BufferedImage contrast(BufferedImage image, int intensity) {
		double averageGray = 0.0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int rawImage = image.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				averageGray += (0.2989 * red + 0.5870 * green + 0.1140 * blue) / (image.getHeight() * image.getWidth());
			}
		}

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int rawImage = image.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;

				// Adjust contrast
				red = (int) (averageGray + ((1 + 0.1 * intensity) * (red - averageGray)));
				green = (int) (averageGray + ((1 + 0.1 * intensity) * (green - averageGray)));
				blue = (int) (averageGray + ((1 + 0.1 * intensity) * (blue - averageGray)));

				// Limit color values to valid range [0, 255]
				red = Math.min(255, Math.max(0, red));
				green = Math.min(255, Math.max(0, green));
				blue = Math.min(255, Math.max(0, blue));

				image.setRGB(j, i, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
		return image;
	}

	/**
	 * Inverts the colors of the given image.
	 *
	 * @param image the image to invert
	 * @return the inverted image
	 */
	public BufferedImage invert(BufferedImage image) {
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int rawImage = image.getRGB(j, i);
				int red = 255 - (rawImage >> 16) & 0xFF;
				int green = 255 - (rawImage >> 8) & 0xFF;
				int blue = 255 - rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;
				image.setRGB(j, i, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
		return image;
	}

	/**
	 * Converts the given image to grayscale.
	 *
	 * @param image the image to convert
	 * @return the grayscale image
	 */
	public BufferedImage grayScale(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int RGB = image.getRGB(i, j);
				int R = (RGB >> 16) & 0xFF;
				int G = (RGB >> 8) & 0xFF;
				int B = RGB & 0xFF;

				int gray = (R + G + B) / 3;

				int grayRGB = (255 << 24) | (gray << 16) | (gray << 8) | gray;
				image.setRGB(i, j, grayRGB);
			}
		}
		return image;
	}
}