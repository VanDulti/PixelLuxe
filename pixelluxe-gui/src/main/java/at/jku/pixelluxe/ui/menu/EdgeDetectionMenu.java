package at.jku.pixelluxe.ui.menu;

import at.jku.pixelluxe.ui.Component;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


public class EdgeDetectionMenu extends JMenu implements Component<JMenu> {
	private final Runnable onXEdge;
	private final Runnable onYEdge;
	private final Runnable onXSobel;
	private final Runnable onYSobel;
	private final Runnable onLaplace;

    public EdgeDetectionMenu(Runnable onXEdge, Runnable onYEdge, Runnable onXSobel, Runnable onYSobel, Runnable onLaplace) {
		super("Edge Detection");
        this.onXEdge = onXEdge;
        this.onYEdge = onYEdge;
        this.onXSobel = onXSobel;
        this.onYSobel = onYSobel;
        this.onLaplace = onLaplace;
    }

    @Override
	public JMenu initialize() {
		JMenuItem xEdgeBtn = new JMenuItem("Horizontal", KeyEvent.VK_1);
		xEdgeBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
		xEdgeBtn.addActionListener(action -> onXEdge.run());

		JMenuItem yEdgeBtn = new JMenuItem("Vertical", KeyEvent.VK_2);
		yEdgeBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK));
		yEdgeBtn.addActionListener(action -> onYEdge.run());


		JMenuItem xSobelBtn = new JMenuItem("Horizontal Sobel", KeyEvent.VK_3);
		xSobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
		xSobelBtn.addActionListener(action -> onXSobel.run());


		JMenuItem ySobelBtn = new JMenuItem("Vertical Sobel", KeyEvent.VK_4);
		ySobelBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK));
		ySobelBtn.addActionListener(action -> onYSobel.run());


		JMenuItem laplaceBtn = new JMenuItem("Laplace", KeyEvent.VK_5);
		laplaceBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK));
		laplaceBtn.addActionListener(action -> onLaplace.run());


		add(xEdgeBtn);
		add(yEdgeBtn);
		add(xSobelBtn);
		add(ySobelBtn);
		add(laplaceBtn);
		return this;
	}
}
