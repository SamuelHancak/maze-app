package sk.stuba.fei.uim.oop;

import javax.swing.JFrame;

public class Window extends JFrame {
    public Window() {
        this.setResizable(false);
        this.setVisible(true);
        this.add(new Panel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 680);
        this.setLocationRelativeTo(null);
    }
}
