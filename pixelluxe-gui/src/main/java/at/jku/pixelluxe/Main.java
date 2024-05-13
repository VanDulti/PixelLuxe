package at.jku.pixelluxe;

import at.jku.pixelluxe.ui.App;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
	public static void main(String[] args) {
		FlatDarkLaf.setup();
		new App().run();
	}
}
