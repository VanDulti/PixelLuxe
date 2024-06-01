package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ContourMenu extends JMenu implements Component<JMenu> {
	private final Runnable onHorizontal;
	private final Runnable onVertical;
	private final Runnable onLaplace;
	private final Runnable onOutline;

	public ContourMenu(Runnable onHorizontal, Runnable onVertical, Runnable onLaplace, Runnable onOutline) {
		super("Contour");
		this.onHorizontal = onHorizontal;
		this.onVertical = onVertical;
		this.onLaplace = onLaplace;
		this.onOutline = onOutline;
	}

	@Override
	public JMenu initialize() {
		JMenuItem xEdgeBtn = new JMenuItem("Horizontal", KeyEvent.VK_1);
		xEdgeBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.SHIFT_DOWN_MASK));
		xEdgeBtn.addActionListener(action -> onHorizontal.run());

		JMenuItem yEdgeBtn = new JMenuItem("Vertical", KeyEvent.VK_2);
		yEdgeBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.SHIFT_DOWN_MASK));
		yEdgeBtn.addActionListener(action -> onVertical.run());

		JMenuItem laplaceBtn = new JMenuItem("Laplace", KeyEvent.VK_3);
		laplaceBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.SHIFT_DOWN_MASK));
		laplaceBtn.addActionListener(action -> onLaplace.run());

		JMenuItem outlineBtn = new JMenuItem("Outline", KeyEvent.VK_4);
		outlineBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.SHIFT_DOWN_MASK));
		outlineBtn.addActionListener(action -> onOutline.run());

		add(xEdgeBtn);
		add(yEdgeBtn);
		add(laplaceBtn);
		add(outlineBtn);
		return this;
	}
}