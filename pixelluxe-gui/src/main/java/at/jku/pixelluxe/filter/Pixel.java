//Todo: Maybe useless!!

package at.jku.pixelluxe.filter;

import java.awt.image.ColorModel;

public class Pixel {

	private final int r, g, b, alpha;

	public Pixel(int imageRaw) {
		r = ColorModel.getRGBdefault().getRed(imageRaw);
		g = ColorModel.getRGBdefault().getGreen(imageRaw);
		b = ColorModel.getRGBdefault().getBlue(imageRaw);
		alpha = ColorModel.getRGBdefault().getAlpha(imageRaw);
	}

	public static int generateRaw(int r, int g, int b, int a) {
		a = a << 24;
		r = r << 16;
		g = g << 8;
		return a | r | g | b;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getAlpha() {
		return alpha;
	}
}
