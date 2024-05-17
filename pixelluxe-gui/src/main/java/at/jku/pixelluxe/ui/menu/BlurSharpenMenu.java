package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class BlurSharpenMenu extends JMenu implements Component<JMenu> {
	private final Runnable onSharpen;
	private final Runnable onGauss;
	private final Runnable onMeanBlur;

    public BlurSharpenMenu(Runnable onSharpen, Runnable onGauss, Runnable onMeanBlur) {
        super("Blur/Sharpen");
        this.onSharpen = onSharpen;
        this.onGauss = onGauss;
        this.onMeanBlur = onMeanBlur;
    }

    @Override
	public JMenu initialize() {
		JMenuItem sharpenBtn = new JMenuItem("Sharpen", KeyEvent.VK_6);
		sharpenBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK));
		sharpenBtn.addActionListener(action -> onSharpen.run());

		JMenuItem gaussBtn = new JMenuItem("Gaussian Blur", KeyEvent.VK_7);
		gaussBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK));
		gaussBtn.addActionListener(action -> onGauss.run());

		JMenuItem meanBlurBtn = new JMenuItem("Mean Blur", KeyEvent.VK_8);
		meanBlurBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK));
		meanBlurBtn.addActionListener(action -> onMeanBlur.run());


		add(sharpenBtn);
		add(gaussBtn);
		add(meanBlurBtn);

		return this;
	}
}
