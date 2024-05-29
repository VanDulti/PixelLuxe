package at.jku.pixelluxe;

import at.jku.pixelluxe.ui.App;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
	public static App app;

	public static void main(String[] args) {
		FlatDarkLaf.setup();
		app = new App();
		app.run();
	}
}
