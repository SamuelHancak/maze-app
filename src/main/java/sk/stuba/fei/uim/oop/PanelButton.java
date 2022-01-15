package sk.stuba.fei.uim.oop;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class PanelButton extends JButton {
    public PanelButton(String name, ActionListener l) {
        setText(name);
        setFocusable(false);
        addActionListener(l);
    }
}
