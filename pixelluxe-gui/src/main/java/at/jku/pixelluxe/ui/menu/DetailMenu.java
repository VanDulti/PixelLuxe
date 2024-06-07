package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * A menu that shall contain all detail-related actions (e.g. sharpen, emboss, etc.).
 */
public class DetailMenu extends JMenu implements Component<JMenu> {
	private final Runnable onSharpen;
	private final Runnable onEmboss;
	private final Runnable onGauss;
	private final Runnable onMeanBlur;

	/**
	 * Creates a new DetailMenu.
	 *
	 * @param onSharpen  the action to perform when the sharpen detail is selected
	 * @param onEmboss   the action to perform when the emboss detail is selected
	 * @param onGauss    the action to perform when the gaussian blur detail is selected
	 * @param onMeanBlur the action to perform when the mean blur detail is selected
	 */
	public DetailMenu(Runnable onSharpen, Runnable onEmboss, Runnable onGauss, Runnable onMeanBlur) {
		super("Detail");
		this.onSharpen = onSharpen;
		this.onEmboss = onEmboss;
		this.onGauss = onGauss;
		this.onMeanBlur = onMeanBlur;
	}

	@Override
	public JMenu initialize() {
		JMenuItem sharpenBtn = new JMenuItem("Sharpen", KeyEvent.VK_1);
		sharpenBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK));
		sharpenBtn.addActionListener(action -> onSharpen.run());

		JMenuItem embossBtn = new JMenuItem("Emboss", KeyEvent.VK_2);
		embossBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK));
		embossBtn.addActionListener(action -> onEmboss.run());

		JMenuItem gaussBtn = new JMenuItem("Gaussian Blur", KeyEvent.VK_3);
		gaussBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK));
		gaussBtn.addActionListener(action -> onGauss.run());

		JMenuItem meanBlurBtn = new JMenuItem("Mean Blur", KeyEvent.VK_4);
		meanBlurBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_DOWN_MASK));
		meanBlurBtn.addActionListener(action -> onMeanBlur.run());

		add(sharpenBtn);
		add(embossBtn);
		add(gaussBtn);
		add(meanBlurBtn);
		return this;
	}
}