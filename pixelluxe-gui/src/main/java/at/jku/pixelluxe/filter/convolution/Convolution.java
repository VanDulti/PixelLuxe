package at.jku.pixelluxe.filter.convolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Convolution {

	public BufferedImage filter(Image image, Kernel kernel){
		int[][] KERNEL = kernel.getMatrix();
		int FACTOR = kernel.getFactor();
		int PAD = (KERNEL.length-1)/2;

		BufferedImage canvas = convertType(image, kernel);
		fillPadding(canvas, kernel);

		int w = canvas.getWidth();
		int h = canvas.getHeight();

		BufferedImage filtered = new BufferedImage(w-PAD, h-PAD, BufferedImage.TYPE_INT_ARGB);

		for(int i = PAD; i < w - PAD; i++){
			for(int j = PAD; j < h - PAD; j++){

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
				filtered.setRGB(i - PAD, j - PAD, filteredRGB);
			}
		}
		return filtered;
	}

	// --------------- helper methods---------------------
	// creates 32bit RGBA integer from 4 channels
	private int genRGB(int R, int G, int B){
		R = Math.max(0, Math.min(R, 255));
		G = Math.max(0, Math.min(G, 255));
		B = Math.max(0, Math.min(B, 255));

		return (255 << 24) | (R << 16) | (G << 8) | B ;
	}

	// converts image to BufferedImage of size: width, height += 2*Padding
	private BufferedImage convertType(Image image, Kernel kernel){
		int PAD = (kernel.getMatrix().length - 1) /2;
		int w = image.getWidth(null) + 2 * PAD;
		int h = image.getHeight(null) + 2 * PAD;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(image, PAD, PAD, null);
		g2d.dispose();

		return bi;
	}

	// fills padding with the closest pixels
	private void fillPadding(BufferedImage canvas, Kernel kernel){
		int PAD = (kernel.getMatrix().length - 1) /2;
		int w = canvas.getWidth();
		int h = canvas.getHeight();

		//------- TOP -> BOTTOM -> LEFT -> RIGHT -------
		// fills top padding with below row of pixels
		for (int i = PAD; i < w - (PAD); i++){
			for(int j = 0; j < PAD; j++){
				canvas.setRGB(i, j, canvas.getRGB(i, j + PAD));
			}
		}
		//fills bottom padding with above row of pixels
		for (int i = PAD; i < w - PAD; i++) {
			for (int j = h - PAD; j < h; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i,j - PAD));
			}
		}
		// fills left hs padding with pixels to the right
		for (int i = 0; i < PAD; i++){
			for(int j = PAD; j < h - PAD; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j));
			}
		}
		// fills right hs padding with pixels to the left
		for (int i = w - PAD; i < w; i++){
			for(int j = PAD; j < h - PAD; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j));
			}
		}

		//---------- TL -> TR -> BL -> BR -----------
		// fills top left corner with top left pixel
		for (int i = 0; i < PAD; i++){
			for(int j = 0; j < PAD; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j + PAD));
			}
		}
		// fills top right corner with top right pixel
		for (int i = w - PAD; i < w; i++){
			for(int j = 0; j < PAD; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j + PAD));
			}
		}
		// fills bottom left corner with bottom left pixel
		for (int i = 0; i < PAD; i++){
			for(int j = h - PAD; j < h; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD, j - PAD));
			}
		}
		// fills bottom right corner with bottom right pixel
		for (int i = w - PAD; i < w; i++){
			for(int j = h - PAD; j < h; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD, j - PAD));
			}
		}
	}
	}
