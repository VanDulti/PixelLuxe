package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class FilterMenu extends JMenu implements Component<JMenu> {
	private final Runnable onInvert;
	private final Runnable onContrast;
	private final Runnable onSaturation;
	private final Runnable onConvolution;

	public FilterMenu(Runnable onInvert, Runnable onContrast, Runnable onSaturation, Runnable onConvolution) {
		super("Filter");
		this.onInvert = onInvert;
		this.onContrast = onContrast;
		this.onSaturation = onSaturation;
		this.onConvolution = onConvolution;
	}

	@Override
	public JMenu initialize() {
		JMenuItem invertBtn = new JMenuItem("Invert", KeyEvent.VK_I);
		invertBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK));
		invertBtn.addActionListener(action -> onInvert.run());

		JMenuItem contrastBtn = new JMenuItem("Contrast", KeyEvent.VK_C);
		contrastBtn.addActionListener(action -> onContrast.run());
		contrastBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK));

		JMenuItem saturationBtn = new JMenuItem("Saturation", KeyEvent.VK_S);
		saturationBtn.addActionListener(action -> onSaturation.run());
		saturationBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));

		JMenuItem convolutionBtn = new JMenuItem("Convolution", KeyEvent.VK_W);
		convolutionBtn.addActionListener(action -> onConvolution.run());
		convolutionBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.ALT_DOWN_MASK));

		add(invertBtn);
		add(contrastBtn);
		add(saturationBtn);
		add(convolutionBtn);
		return this;
	}


}
