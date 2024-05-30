package at.jku.pixelluxe.filter.convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Convolution {

	public BufferedImage filter(Image image, Kernel kernel){
		BufferedImage canvas = convertType(image, kernel);
		fillPadding(canvas, kernel);
		return convolve(canvas, kernel);
	}

	public BufferedImage filter(BufferedImage canvas, Kernel kernel){
		fillPadding(canvas, kernel);
		return convolve(canvas, kernel);
	}

	public BufferedImage convolve(BufferedImage canvas, Kernel kernel) {
		int[][] KERNEL = kernel.getMatrix();
		int FACTOR = kernel.getFactor();
		int PAD = (KERNEL.length - 1) / 2;

		if (kernel.getType().equals("edge") || kernel.getType().startsWith("sobel")) {
			canvas = convertToGrayScale(canvas);
		}

		if(kernel.getType().startsWith("sobel") && !kernel.getIsTransformed()) {
			KERNEL = transposeAndInvert(kernel);
		}

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

						// Multiply each color component by the corresponding kernel coefficient
						sumR += KERNEL[k][l] * red;
						sumG += KERNEL[k][l] * green;
						sumB += KERNEL[k][l] * blue;
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

	// converts image to bufferedImage, adds EMPTY padding
	private BufferedImage convertType(Image image, Kernel kernel) {
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

	private int[][] transposeAndInvert(Kernel kernel){
		int[][] matrix = kernel.getMatrix();
		int l = matrix.length;

		int[][] transMatrix = new int[l][l];
		int[][] invMatrix = new int[l][l];

		// transposes the matrix
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				transMatrix[i][j] = matrix[j][i];
			}
		}

		// inverts matrix horizontally for top/bottom and vertically for left/right
		if(kernel.getType().equals("sobelV")){
			for(int i = 0; i < l; i++){
				for(int j = 0; j < l; j++){
					invMatrix[i][j] = transMatrix[i][l - 1 - j];
				}
			}
		} else {
			for(int i = 0; i < l; i++){
                System.arraycopy(transMatrix[l - 1 - i], 0, invMatrix[i], 0, l);
			}
		}
		kernel.setIsTransformed(true);
		return invMatrix;
	}
}