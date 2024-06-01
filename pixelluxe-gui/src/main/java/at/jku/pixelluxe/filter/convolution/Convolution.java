package at.jku.pixelluxe.filter.convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Convolution {
	public BufferedImage filter(BufferedImage canvas, Kernel kernel, int intensity) {
		int[][] KERNEL = kernel.getMatrix();
		String type = kernel.getType();
		fillPadding(addPadding(canvas, kernel), kernel);

		if (intensity == 0) {
			return canvas;
		}
		if (!type.equals("detail")) {
			canvas = convertToGrayScale(canvas);
		}
		if (type.startsWith("sobel")) {
			KERNEL = transposeAndInvert(kernel);
		}

		if (type.equals("detail")) {
			while (intensity > 1) {
				canvas = applyConvolution(canvas, KERNEL, kernel, 1);
				intensity--;
				fillPadding(canvas, kernel);
			}
		}
		return applyConvolution(canvas, KERNEL, kernel, intensity);
	}

	private BufferedImage applyConvolution(BufferedImage canvas, int[][] KERNEL, Kernel kernel, int intensity) {
		double FACTOR = kernel.getFactor();
		int PAD = (kernel.getMatrix().length - 1) / 2;

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
						sumR += (int) KERNEL[k][l] * intensity * red;
						sumG += (int) KERNEL[k][l] * intensity * green;
						sumB += (int) KERNEL[k][l] * intensity * blue;
					}
				}

				int r = (int) Math.min(Math.max((sumR / FACTOR), 0), 255);
				int g = (int) Math.min(Math.max((sumG / FACTOR), 0), 255);
				int b = (int) Math.min(Math.max((sumB / FACTOR), 0), 255);

				int filteredRGB = genRGB(r, g, b);
				filtered.setRGB(i, j, filteredRGB);
			}
		}
		return filtered;
	}

	// converts image to bufferedImage, adds EMPTY padding
	private BufferedImage addPadding(Image image, Kernel kernel) {
		int PAD = (kernel.getMatrix().length - 1) / 2;
		int w = image.getWidth(null) + 2 * PAD;
		int h = image.getHeight(null) + 2 * PAD;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(image, PAD, PAD, null);
		g2d.dispose();

		return bi;
	}

	// converts bufferedImage to gray scale for edge/contrast related kernels
	private BufferedImage convertToGrayScale(BufferedImage bi) {
		int w = bi.getWidth();
		int h = bi.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int RGB = bi.getRGB(i, j);
				int R = (RGB >> 16) & 0xFF;
				int G = (RGB >> 8) & 0xFF;
				int B = RGB & 0xFF;

				int gray = (R + G + B) / 3;
				bi.setRGB(i, j, genRGB(gray, gray, gray));
			}
		}
		return bi;
	}

	// fills padding with the closest pixels
	private void fillPadding(BufferedImage canvas, Kernel kernel) {
		int PAD = (kernel.getMatrix().length - 1) / 2;
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

	// creates 32bit RGBA integer from 4 channels
	private int genRGB(int R, int G, int B) {
		R = Math.max(0, Math.min(R, 255));
		G = Math.max(0, Math.min(G, 255));
		B = Math.max(0, Math.min(B, 255));

		return (255 << 24) | (R << 16) | (G << 8) | B;
	}

	private int[][] transposeAndInvert(Kernel kernel) {
		int[][] matrix = kernel.getMatrix();
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
		if (kernel.getType().equals("sobelV")) {
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
}