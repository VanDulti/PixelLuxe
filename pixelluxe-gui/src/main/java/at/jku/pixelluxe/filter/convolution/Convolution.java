package at.jku.pixelluxe.filter.convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Convolution class for image manipulation. Offers ways to apply convolution filters to images. These operations can be
 * expensive, so think about parallelizing them.
 */
public class Convolution {
	/**
	 * Applies the given kernel to the given image with the given intensity.
	 *
	 * @param canvas    the image to manipulate
	 * @param kernel    the kernel to apply
	 * @param intensity the intensity of the kernel application
	 * @return the manipulated image
	 */
	public BufferedImage filter(BufferedImage canvas, Kernel kernel, int intensity) {
		int[][] KERNEL = transposeAndInvert(kernel);
		KernelType type = kernel.type();
		canvas = addPadding(canvas, kernel);
		fillPadding(canvas, kernel);

		if (intensity == 0) {
			return canvas;
		}
		if (type != KernelType.DETAIL) {
			canvas = convertToGrayScale(canvas);
		}

		if (type == KernelType.DETAIL) {
			while (intensity > 1) {
				canvas = applyConvolution(canvas, KERNEL, kernel, 1);
				intensity--;
				fillPadding(canvas, kernel);
			}
		}
		return applyConvolution(canvas, KERNEL, kernel, intensity);
	}

	private int[][] transposeAndInvert(Kernel kernel) {
		int[][] matrix = kernel.matrix();
		int l = matrix.length;

		int[][] transMatrix = new int[l][l];
		int[][] invMatrix = new int[l][l];

		// transposes the matrix
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				transMatrix[i][j] = matrix[j][i];
			}
		}

		// inverts matrix horizontally for top/bottom sobel and vertically for left/right sobel
		if (kernel.type() == KernelType.SOBEL_V) {
			for (int i = 0; i < l; i++) {
				for (int j = 0; j < l; j++) {
					invMatrix[i][j] = transMatrix[i][l - 1 - j];
				}
			}
		} else {
			for (int i = 0; i < l; i++) {
				System.arraycopy(transMatrix[l - 1 - i], 0, invMatrix[i], 0, l);
			}
		}
		return invMatrix;
	}

	/**
	 * Adds empty padding to the given image.
	 *
	 * @param image  the image to add padding to
	 * @param kernel the kernel to apply
	 * @return the image with padding
	 */
	private BufferedImage addPadding(Image image, Kernel kernel) {
		int PAD = (kernel.matrix().length - 1) / 2;
		int w = image.getWidth(null) + 2 * PAD;
		int h = image.getHeight(null) + 2 * PAD;

		BufferedImage withPadding = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = withPadding.createGraphics();
		g2d.drawImage(image, PAD, PAD, null);
		g2d.dispose();

		return withPadding;
	}

	/**
	 * Fills the padding of the given image with the closest pixels.
	 *
	 * @param canvas the image to fill the padding with
	 * @param kernel the kernel to apply
	 */
	private void fillPadding(BufferedImage canvas, Kernel kernel) {
		int PAD = (kernel.matrix().length - 1) / 2;
		int w = canvas.getWidth();
		int h = canvas.getHeight();

		//------------------- STRAIGHT BORDERS -------------------
		// top
		for (int i = PAD; i < w - (PAD); i++) {
			for (int j = 0; j < PAD; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i, j + PAD));
			}
		}
		// bottom
		for (int i = PAD; i < w - PAD; i++) {
			for (int j = h - PAD; j < h; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i, j - PAD));
			}
		}
		// left
		for (int i = 0; i < PAD; i++) {
			for (int j = PAD; j < h - PAD; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j));
			}
		}
		// right
		for (int i = w - PAD; i < w; i++) {
			for (int j = PAD; j < h - PAD; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j));
			}
		}

		//------------------------- CORNERS -------------------------
		// top left
		for (int i = 0; i < PAD; i++) {
			for (int j = 0; j < PAD; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j + PAD));
			}
		}
		// top right
		for (int i = w - PAD; i < w; i++) {
			for (int j = 0; j < PAD; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j + PAD));
			}
		}
		// bottom left
		for (int i = 0; i < PAD; i++) {
			for (int j = h - PAD; j < h; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j - PAD));
			}
		}
		// bottom right
		for (int i = w - PAD; i < w; i++) {
			for (int j = h - PAD; j < h; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j - PAD));
			}
		}
	}

	/**
	 * Converts the given image to grayscale for edge/contrast related kernels.
	 *
	 * @param image the image to convert
	 * @return the grayscale image
	 */
	private BufferedImage convertToGrayScale(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage grayScaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int RGB = image.getRGB(i, j);
				int R = (RGB >> 16) & 0xFF;
				int G = (RGB >> 8) & 0xFF;
				int B = RGB & 0xFF;

				int gray = (R + G + B) / 3;
				grayScaled.setRGB(i, j, genRGB(gray, gray, gray));
			}
		}
		return grayScaled;
	}

	/**
	 * Actually applies the given kernel to the given image with the given intensity.
	 */
	private BufferedImage applyConvolution(BufferedImage canvas, int[][] KERNEL, Kernel kernel, int intensity) {
		int FACTOR = kernel.factor();
		int PAD = (kernel.matrix().length - 1) / 2;

		int w = canvas.getWidth();
		int h = canvas.getHeight();

		BufferedImage filtered = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = PAD; i < w - PAD; i++) {
			for (int j = PAD; j < h - PAD; j++) {
				int sumR = 0;
				int sumG = 0;
				int sumB = 0;

				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++) {
						int RGB = canvas.getRGB(i + k - 1, j + l - 1);
						int red = (RGB >> 16) & 0xFF;
						int green = (RGB >> 8) & 0xFF;
						int blue = RGB & 0xFF;

						// Multiply each color component by the corresponding kernel entry
						sumR += KERNEL[k][l] * intensity * red;
						sumG += KERNEL[k][l] * intensity * green;
						sumB += KERNEL[k][l] * intensity * blue;
					}
				}

				int r = Math.min(Math.max((sumR / FACTOR), 0), 255);
				int g = Math.min(Math.max((sumG / FACTOR), 0), 255);
				int b = Math.min(Math.max((sumB / FACTOR), 0), 255);

				int filteredRGB = genRGB(r, g, b);
				filtered.setRGB(i, j, filteredRGB);
			}
		}
		return filtered;
	}

	/**
	 * Generates a 32bit RGBA integer from the given color channels.
	 *
	 * @param R the red channel
	 * @param G the green channel
	 * @param B the blue channel
	 * @return the 32bit RGBA integer
	 */
	private int genRGB(int R, int G, int B) {
		R = Math.max(0, Math.min(R, 255));
		G = Math.max(0, Math.min(G, 255));
		B = Math.max(0, Math.min(B, 255));

		return (255 << 24) | (R << 16) | (G << 8) | B;
	}
}