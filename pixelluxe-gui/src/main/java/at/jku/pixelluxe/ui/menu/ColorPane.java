package at.jku.pixelluxe.ui.menu;

import javax.swing.*;
import java.awt.*;

public class ColorPane {

	private final JButton toplevelComp;

	public ColorPane() {

		JButton btn = new JButton("     ");
		btn.setBackground(Color.WHITE);
		toplevelComp = btn;
	}

	public void addTo(JComponent component) {
		component.add(toplevelComp);
	}

	public JButton getComponent() {
		return toplevelComp;
	}


}
