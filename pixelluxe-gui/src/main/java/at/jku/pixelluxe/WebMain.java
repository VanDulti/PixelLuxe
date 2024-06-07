package at.jku.pixelluxe;

import at.jku.pixelluxe.ui.App;

public class WebMain {
	/**
	 * Starts PixelLuxe without loading flatlaf. This is used for the web version, as flatlaf is not supported there.
	 */
	public static void main(String[] args) {
		new App().run();
	}
}
