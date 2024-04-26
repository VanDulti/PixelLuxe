package at.jku.pixelluxe.filter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Convolution {

	private int[][] KERNEL = {
		{1, 0, -1},
		{1, 0, -1},
		{1, 0, -1}
	};
	private int PAD_X = 1;
	private int PAD_Y = 1;

	public BufferedImage filter(Image image){

		// transforms original image to BufferedImage and adds EMPTY Padding
		BufferedImage canvas = convertType(image);
		fillPadding(canvas);

		//TODO implement kernel multiplication

		return canvas;
	}


	// helper methods for better readability of filter()
	private int genRGB(int R, int G, int B, int A){
		R = Math.max(0, Math.min(R, 255));
		G = Math.max(0, Math.min(G, 255));
		B = Math.max(0, Math.min(B, 255));
		A = Math.max(0, Math.min(A, 255));

		return (A << 24) | (R << 16) | (G << 8) | B ;
	}

	private BufferedImage convertType(Image image){
		int w = image.getWidth(null) + 2 * PAD_X;
		int h = image.getHeight(null) + 2 * PAD_Y;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(image, PAD_X, PAD_Y, null);
		g2d.dispose();

		return bi;
	}

	private void fillPadding(BufferedImage canvas){
		int w = canvas.getWidth();
		int h = canvas.getHeight();

		//------- TOP -> BOTTOM -> LEFT -> RIGHT -------
		// fills top padding with below row of pixels
		for (int i = PAD_X; i < w - (PAD_X); i++){
			for(int j = 0; j < PAD_Y; j++){
				canvas.setRGB(i, j, canvas.getRGB(i, j + PAD_Y));
			}
		}
		//fills bottom padding with above row of pixels
		for (int i = PAD_X; i < w - PAD_X; i++) {
			for (int j = h - PAD_Y; j < h; j++) {
				canvas.setRGB(i, j, canvas.getRGB(i,j - PAD_Y));
			}
		}
		// fills left hs padding with pixels to the right
		for (int i = 0; i < PAD_X; i++){
			for(int j = PAD_Y; j < h - PAD_Y; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD_X, j));
			}
		}
		// fills right hs padding with pixels to the left
		for (int i = w - PAD_X; i < w; i++){
			for(int j = PAD_Y; j < h - PAD_Y; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD_X, j));
			}
		}

		//---------- TL -> TR -> BL -> BR -----------
		// fills top left corner with top left pixel
		for (int i = 0; i < PAD_X; i++){
			for(int j = 0; j < PAD_Y; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD_X, j + PAD_Y));
			}
		}
		// fills top right corner with top right pixel
		for (int i = w - PAD_X; i < w; i++){
			for(int j = 0; j < PAD_Y; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD_X, j + PAD_Y));
			}
		}
		// fills bottom left corner with bottom left pixel
		for (int i = 0; i < PAD_X; i++){
			for(int j = h - PAD_Y; j < h; j++){
				canvas.setRGB(i, j, canvas.getRGB(i + PAD_X, j - PAD_Y));
			}
		}
		// fills bottom right corner with bottom right pixel
		for (int i = w - PAD_X; i < w; i++){
			for(int j = h - PAD_Y; j < h; j++){
				canvas.setRGB(i, j, canvas.getRGB(i - PAD_X, j - PAD_Y));
			}
		}
	}
}
