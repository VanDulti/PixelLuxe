package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.App;
import at.jku.pixelluxe.ui.tools.ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorPane {

	private JButton toplevelComp;
	public ColorPane() {

		JButton btn = new JButton("     ");
		btn.setBackground(Color.WHITE);
//		btn.setMinimumSize(new Dimension(widht, height));
		toplevelComp = btn;
	}

	public void addTo(JComponent component) {
		component.add(toplevelComp);
	}

	public JButton getComponent() {
		return toplevelComp;
	}


}
