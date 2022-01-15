package sk.stuba.fei.uim.oop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveButton extends PanelButton implements ActionListener {
    private final char move;
    private transient ButtonMoveListener movement;

    public MoveButton(String name, ActionListener l, char move) {
        super(name, l);

        this.move = move;
        addActionListener(this);
        addButtonsMovementListener((ButtonMoveListener) l);
    }

    private void addButtonsMovementListener(ButtonMoveListener l) {
        movement = l;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movement.playerMove(move);
    }
}
