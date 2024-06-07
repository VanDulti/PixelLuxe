package at.jku.pixelluxe;

import at.jku.pixelluxe.ui.App;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
	public static App app;

	/**
	 * Starts the PixelLuxe application.
	 */
	public static void main(String[] args) {
		FlatDarkLaf.setup();
		app = new App();
		app.run();
	}
}
