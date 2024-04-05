package at.jku.pixelluxe;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.initialize();
        JPanel mainPanel = mainForm.getMainPanel();

        JFrame mainFrame = new JFrame("PixelLuxe");
        mainFrame.setContentPane(mainPanel);
        mainFrame.setResizable(true);
        mainFrame.setSize(200, 100);
        mainFrame.setVisible(true);

        System.out.println("Hello World!");
    }
}