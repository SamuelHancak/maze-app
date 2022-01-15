package sk.stuba.fei.uim.oop;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameBoard extends JPanel implements MouseInputListener, KeyListener {
    /**
     * For keyboard movement use 'wsad' keys
     */
    /**
     * +1 px of with and height because of "paddings" between fields and board
     * boarders
     */
    private static final int BOARD_WIDTH = 481;
    private static final int BOARD_HEIGHT = 481;
    /**
     * count of rows and columns (since board is a rectangle they will be the same)
     */
    private static final int BOARD_FIELDS = 20;
    /**
     * size of the individual fields on the board
     */
    private static final int FIELD_SIZE = 24;
    private final transient Field[][] map;
    private final transient Deque<Field> stack;
    private final Random random;
    private transient Field currentField;
    private transient EndGameListener panel;
    private final transient ArrayList<Field> allowedFields;
    private transient Field hoveredField;
    private boolean isFocused;

    public GameBoard() {
        map = new Field[BOARD_FIELDS][BOARD_FIELDS];
        stack = new ArrayDeque<>();
        random = new Random();
        allowedFields = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        initializeMap();
    }

    protected void initializeMap() {
        for (int i = 0; i < BOARD_FIELDS; ++i) {
            for (int j = 0; j < BOARD_FIELDS; ++j) {
                map[i][j] = new Field(i, j);
                map[i][j].addMouseListener(this);
            }
        }
        currentField = map[0][0];
        currentField.setCurrent();
        currentField.setVisited();
        hoveredField = currentField;
        isFocused = false;

        createMaze(currentField);
        findAllowedFields(currentField);
    }

    private void createMaze(Field field) {
        Field next;

        do {
            next = checkNeighbours(field);

            if (next != null) {
                stack.push(field);
                next.setVisited();
                removeBorder(field, next);
                field = next;
            } else {
                field = stack.pop();
            }

        } while (!stack.isEmpty());
    }

    private void setBorderFalse(int index, Field field) {
        field.getBorders().set(index, Boolean.FALSE);
    }

    private void removeBorder(Field current, Field next) {
        int x = (current.getXCoord()) - (next.getXCoord());

        if (x == 1) {
            setBorderFalse(3, current);
            setBorderFalse(1, next);
        } else if (x == -1) {
            setBorderFalse(1, current);
            setBorderFalse(3, next);
        }

        int y = (current.getYCoord()) - (next.getYCoord());

        if (y == 1) {
            setBorderFalse(0, current);
            setBorderFalse(2, next);
        } else if (y == -1) {
            setBorderFalse(2, current);
            setBorderFalse(0, next);
        }
    }

    private Field checkNeighbours(Field field) {
        field.clearNeighbourFields();
        int x = field.getXCoord();
        int y = field.getYCoord();
        Field neighbour;

        if (x - 1 >= 0) {
            neighbour = map[x - 1][y];
            if (!neighbour.isVisited()) {
                field.addNeighbourFields(neighbour);
            }
        }

        if (x + 1 < BOARD_FIELDS) {
            neighbour = map[x + 1][y];
            if (!neighbour.isVisited()) {
                field.addNeighbourFields(neighbour);
            }
        }

        if (y - 1 >= 0) {
            neighbour = map[x][y - 1];
            if (!neighbour.isVisited()) {
                field.addNeighbourFields(neighbour);
            }
        }

        if (y + 1 < BOARD_FIELDS) {
            neighbour = map[x][y + 1];
            if (!neighbour.isVisited()) {
                field.addNeighbourFields(neighbour);
            }
        }

        if (!field.getNeighbourFields().isEmpty()) {
            int neighbourIndex = random.nextInt(field.getNeighbourFields().size());

            return field.getNeighbourFields().get(neighbourIndex);
        } else {
            return null;
        }
    }

    public boolean checkEnd() {
        return currentField == map[BOARD_FIELDS - 1][BOARD_FIELDS - 1];
    }

    private void checkAllowed(int xCoord, int yCoord, int index, boolean swap, boolean greater) {
        Field nextField;

        do {
            if (swap) {
                xCoord = xCoord ^ yCoord ^ (yCoord = xCoord);
            }

            if (greater ? (yCoord > 0) : (yCoord + 1 < BOARD_FIELDS)) {
                yCoord += greater ? -1 : 1;
                if (swap) {
                    xCoord = xCoord ^ yCoord ^ (yCoord = xCoord);
                }
                nextField = map[xCoord][yCoord];
                nextField.setHoverable(true);
                allowedFields.add(nextField);
            } else {
                nextField = null;
            }
        } while (nextField != null && Boolean.FALSE.equals(nextField.getBorders().get(index)));
    }

    private void findAllowedFields(Field field) {
        for (var fieldd : allowedFields) {
            fieldd.setHoverable(false);
        }
        allowedFields.clear();

        int x = field.getXCoord();
        int y = field.getYCoord();
        int index = 0;

        for (var border : field.getBorders()) {
            if (Boolean.FALSE.equals(border)) {
                checkAllowed(x, y, index, (index % 2) != 0, (index % 3) == 0);

                x = field.getXCoord();
                y = field.getYCoord();
            }
            ++index;
        }
    }

    protected void movement(char move) {
        switch (move) {
        case 'd':
            if (currentField.getXCoord() + 1 < 20) {
                checkMovement(map[currentField.getXCoord() + 1][currentField.getYCoord()]);
            }
            break;
        case 'a':
            if (currentField.getXCoord() - 1 >= 0) {
                checkMovement(map[currentField.getXCoord() - 1][currentField.getYCoord()]);
            }
            break;
        case 'w':
            if (currentField.getYCoord() - 1 >= 0) {
                checkMovement(map[currentField.getXCoord()][currentField.getYCoord() - 1]);
            }
            break;
        case 's':
            if (currentField.getYCoord() + 1 < 20) {
                checkMovement(map[currentField.getXCoord()][currentField.getYCoord() + 1]);
            }
            break;
        default:
            break;
        }

        findAllowedFields(currentField);
        hoveredField.setHovered(false);
    }

    protected void addEndGameListener(EndGameListener listener) {
        panel = listener;
    }

    private void checkMovement(Field field) {
        if (allowedFields.contains(field)) {
            currentField.setCurrent();
            currentField = field;
            currentField.setCurrent();

            repaint();

            if (checkEnd()) {
                panel.gameEnd();
            }
        }
    }

    private void draw(Graphics g) {
        for (int i = 0; i < BOARD_FIELDS; ++i) {
            for (int j = 0; j < BOARD_FIELDS; ++j) {
                map[i][j].draw(g);
            }
        }

        requestFocus();
    }

    @Override
    public void paint(Graphics g) {
        setBounds(55, 125, BOARD_WIDTH, BOARD_HEIGHT);

        draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Field clickedField = map[e.getX() / FIELD_SIZE][e.getY() / FIELD_SIZE];

        if (isFocused) {
            checkMovement(clickedField);
            findAllowedFields(currentField);
            isFocused = false;
        } else if (clickedField.equals(currentField)) {
            isFocused = true;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (hoveredField.isHovered()) {
            hoveredField.setHovered(false);

            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / FIELD_SIZE;
        int y = e.getY() / FIELD_SIZE;

        if (x < BOARD_FIELDS && y < BOARD_FIELDS) {
            Field field = map[x][y];

            if (field != hoveredField && isFocused) {
                hoveredField.setHovered(false);
                hoveredField = field;
                hoveredField.setHovered(true);

                repaint();
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent e) {
        movement(e.getKeyChar());

        if (checkEnd()) {
            panel.gameEnd();
        }
    }
}
