package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class SobelMenu extends JMenu implements Component<JMenu>{
	private final Runnable onTopSobel;
	private final Runnable onBottomSobel;
	private final Runnable onLeftSobel;
	private final Runnable onRightSobel;

	public SobelMenu(Runnable OnTopSobel, Runnable onBottomSobel, Runnable onLeftSobel, Runnable onRightSobel) {
		super("Sobel");
		this.onTopSobel = OnTopSobel;
		this.onBottomSobel = onBottomSobel;
		this.onLeftSobel = onLeftSobel;
		this.onRightSobel = onRightSobel;
	}

	@Override
	public JMenu initialize() {
		JMenuItem topSobelBtn = new JMenuItem("Top", KeyEvent.VK_1);
		topSobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
		topSobelBtn.addActionListener(action -> onTopSobel.run());

		JMenuItem bottomSobelBtn = new JMenuItem("Bottom", KeyEvent.VK_2);
		bottomSobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK));
		bottomSobelBtn.addActionListener(action -> onBottomSobel.run());

		JMenuItem leftSobelBtn = new JMenuItem("Left", KeyEvent.VK_3);
		leftSobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
		leftSobelBtn.addActionListener(action -> onLeftSobel.run());

		JMenuItem rightSobelBtn = new JMenuItem("Right", KeyEvent.VK_4);
		rightSobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK));
		rightSobelBtn.addActionListener(action -> onRightSobel.run());

		add(topSobelBtn);
		add(bottomSobelBtn);
		add(leftSobelBtn);
		add(rightSobelBtn);
		return this;
	}
}