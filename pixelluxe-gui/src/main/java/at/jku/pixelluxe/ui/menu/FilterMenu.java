package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * A menu that shall contain all regular filter-related actions (e.g. invert, contrast, etc.).
 */
public class FilterMenu extends JMenu implements Component<JMenu> {
	private final Runnable onInvert;
	private final Runnable onContrast;
	private final Runnable onSaturation;
	private final Runnable onGrayScale;

	/**
	 * Creates a new FilterMenu.
	 *
	 * @param onInvert     the action to perform when the invert filter is selected
	 * @param onContrast   the action to perform when the contrast filter is selected
	 * @param onSaturation the action to perform when the saturation filter is selected
	 * @param onGrayScale  the action to perform when the gray scale filter is selected
	 */
	public FilterMenu(Runnable onInvert, Runnable onContrast, Runnable onSaturation, Runnable onGrayScale) {
		super("Filter");
		this.onInvert = onInvert;
		this.onContrast = onContrast;
		this.onSaturation = onSaturation;
		this.onGrayScale = onGrayScale;
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

		JMenuItem grayScaleBtn = new JMenuItem("Gray Scale", KeyEvent.VK_G);
		grayScaleBtn.addActionListener(action -> onGrayScale.run());
		grayScaleBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK));

		add(invertBtn);
		add(contrastBtn);
		add(saturationBtn);
		add(grayScaleBtn);
		return this;
	}
}