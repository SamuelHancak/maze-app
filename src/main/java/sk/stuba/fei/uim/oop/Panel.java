package sk.stuba.fei.uim.oop;

import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel extends JPanel implements ActionListener, EndGameListener, ButtonMoveListener {
    private final GameBoard gameBoard;
    private final PanelButton resetBtn;
    private int wins;
    JLabel winsCounterLabel;

    public Panel() {
        wins = 0;
        winsCounterLabel = new JLabel("New game");
        this.setLayout(new BorderLayout());

        var panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(2, 0));

        winsCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        var panelButtonsGroup = new JPanel();
        panelButtonsGroup.setLayout(new GridLayout(1, 5));

        JButton upBtn;
        JButton downBtn;
        JButton rightBtn;
        JButton leftBtn;

        resetBtn = new PanelButton("Reset", this);
        leftBtn = new MoveButton("LEFT", this, 'a');
        upBtn = new MoveButton("UP", this, 'w');
        downBtn = new MoveButton("DOWN", this, 's');
        rightBtn = new MoveButton("RIGHT", this, 'd');

        var verticalBtns = new JPanel();
        verticalBtns.setLayout(new GridLayout(2, 0));
        verticalBtns.add(upBtn);
        verticalBtns.add(downBtn);

        panelButtonsGroup.add(resetBtn);
        panelButtonsGroup.add(leftBtn);
        panelButtonsGroup.add(verticalBtns);
        panelButtonsGroup.add(rightBtn);

        panelTop.add(winsCounterLabel);
        panelTop.add(panelButtonsGroup);

        gameBoard = new GameBoard();
        gameBoard.addEndGameListener(this);

        this.add(gameBoard, BorderLayout.CENTER);
        this.add(panelTop, BorderLayout.PAGE_START);
    }

    private void restartGame() {
        gameBoard.initializeMap();
        gameBoard.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetBtn) {
            winsCounterLabel.setText("New game");
            wins = 0;
            restartGame();
        }

        if (gameBoard.checkEnd()) {
            gameEnd();
        }
    }

    @Override
    public void gameEnd() {
        winsCounterLabel.setText(("Games won: " + ++wins));
        restartGame();
    }

    @Override
    public void playerMove(char move) {
        gameBoard.movement(move);
    }
}
