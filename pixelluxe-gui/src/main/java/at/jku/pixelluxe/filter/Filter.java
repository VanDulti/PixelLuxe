package at.jku.pixelluxe.filter;

import java.awt.image.BufferedImage;

public class Filter {
	public BufferedImage saturation(BufferedImage bi, int intensity) {
		for (int i = 0; i < bi.getHeight(); i++) {
			for (int j = 0; j < bi.getWidth(); j++) {
				int rawImage = bi.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;
				float[] hsb = java.awt.Color.RGBtoHSB(red, green, blue, null);
				hsb[1] *= 1 + 0.1 * intensity;
				hsb[1] = Math.max(0, Math.min(hsb[1], 1));
				bi.setRGB(j, i, (alpha << 24) | (java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0x00FFFFFF));
			}
		}
		return bi;
	}

	public BufferedImage contrast(BufferedImage bi, int intensity) {

		double averageGray = 0.0;
		for (int i = 0; i < bi.getHeight(); i++) {
			for (int j = 0; j < bi.getWidth(); j++) {
				int rawImage = bi.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				averageGray += (0.2989 * red + 0.5870 * green + 0.1140 * blue) / (bi.getHeight() * bi.getWidth());
			}
		}

		for (int i = 0; i < bi.getHeight(); i++) {
			for (int j = 0; j < bi.getWidth(); j++) {
				int rawImage = bi.getRGB(j, i);
				int red = (rawImage >> 16) & 0xFF;
				int green = (rawImage >> 8) & 0xFF;
				int blue = rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;

				// Contrast adjust
				red = (int) (averageGray + ((1 + 0.1 * intensity) * (red - averageGray)));
				green = (int) (averageGray + ((1 + 0.1 * intensity) * (green - averageGray)));
				blue = (int) (averageGray + ((1 + 0.1 * intensity) * (blue - averageGray)));

				// Farbwerte auf gültigen Bereich [0, 255] begrenzen
				red = Math.min(255, Math.max(0, red));
				green = Math.min(255, Math.max(0, green));
				blue = Math.min(255, Math.max(0, blue));

				bi.setRGB(j, i, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
		return bi;
	}

	public BufferedImage invert(BufferedImage bi) {
		for (int i = 0; i < bi.getHeight(); i++) {
			for (int j = 0; j < bi.getWidth(); j++) {
				int rawImage = bi.getRGB(j, i);
				int red = 255 - (rawImage >> 16) & 0xFF;
				int green = 255 - (rawImage >> 8) & 0xFF;
				int blue = 255 - rawImage & 0xFF;
				int alpha = (rawImage >> 24) & 0xFF;
				bi.setRGB(j, i, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
		return bi;
	}

	public BufferedImage grayScale(BufferedImage bi) {
		int w = bi.getWidth();
		int h = bi.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int RGB = bi.getRGB(i, j);
				int R = (RGB >> 16) & 0xFF;
				int G = (RGB >> 8) & 0xFF;
				int B = RGB & 0xFF;

				int gray = (R + G + B) / 3;
				R = Math.max(0, Math.min(gray, 255));

				int grayRGB = (255 << 24) | (gray << 16) | (gray << 8) | gray;
				bi.setRGB(i, j, grayRGB);
			}
		}
		return bi;
	}
}