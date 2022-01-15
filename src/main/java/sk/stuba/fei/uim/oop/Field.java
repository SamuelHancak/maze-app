package sk.stuba.fei.uim.oop;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Field extends Canvas {

    private static final int FIELD_SIZE = 24;
    private final int xCoord;
    private final int yCoord;
    private boolean visited;
    private boolean isCurrentField;
    private boolean hovered;
    private boolean hoverable;
    private ArrayList<Boolean> borders;
    private ArrayList<Field> neighbourFields;

    public Field(int x, int y) {
        xCoord = x * FIELD_SIZE;
        yCoord = y * FIELD_SIZE;
        visited = false;
        isCurrentField = false;
        hovered = false;
        hoverable = false;
        neighbourFields = new ArrayList<>();
        borders = new ArrayList<>(Arrays.asList(new Boolean[4]));
        Collections.fill(borders, Boolean.TRUE);
    }

    protected ArrayList<Boolean> getBorders() {
        return borders;
    }

    protected ArrayList<Field> getNeighbourFields() {
        return neighbourFields;
    }

    protected void addNeighbourFields(Field field) {
        neighbourFields.add(field);
    }

    protected void clearNeighbourFields() {
        neighbourFields.clear();
    }

    protected boolean isVisited() {
        return visited;
    }

    protected void setVisited() {
        visited = true;
    }

    protected int getXCoord() {
        return xCoord / FIELD_SIZE;
    }

    protected int getYCoord() {
        return yCoord / FIELD_SIZE;
    }

    protected boolean isHoverable() {
        return hoverable;
    }

    protected void setHoverable(boolean value) {
        hoverable = value;
    }

    protected boolean isHovered() {
        return hovered;
    }

    protected void setHovered(boolean value) {
        hovered = value;
    }

    protected void setCurrent() {
        isCurrentField = !isCurrentField;
    }

    protected void draw(Graphics g) {
        g.setColor(Color.black);

        if (xCoord == 19 * FIELD_SIZE && yCoord == 19 * FIELD_SIZE) {
            g.setColor(Color.cyan);
        } else if (visited) {
            g.setColor(Color.white);
        }

        g.fillRect(xCoord, yCoord, FIELD_SIZE, FIELD_SIZE);

        if (isHovered() && isHoverable()) {
            g.setColor(Color.green);
            g.fillOval(xCoord, yCoord, FIELD_SIZE, FIELD_SIZE);
        }

        if (isCurrentField) {
            g.setColor(Color.orange);
            g.fillOval(xCoord, yCoord, FIELD_SIZE, FIELD_SIZE);
        }

        g.setColor(Color.black);

        addBorders(g);
    }

    private void addBorders(Graphics g) {
        if (Boolean.TRUE.equals(borders.get(0))) {
            g.drawLine(xCoord, yCoord, xCoord + FIELD_SIZE, yCoord);
        }

        if (Boolean.TRUE.equals(borders.get(1))) {
            g.drawLine(xCoord + FIELD_SIZE, yCoord, xCoord + FIELD_SIZE, yCoord + FIELD_SIZE);
        }

        if (Boolean.TRUE.equals(borders.get(2))) {
            g.drawLine(xCoord, yCoord + FIELD_SIZE, xCoord + FIELD_SIZE, yCoord + FIELD_SIZE);
        }

        if (Boolean.TRUE.equals(borders.get(3))) {
            g.drawLine(xCoord, yCoord, xCoord, yCoord + FIELD_SIZE);
        }
    }
}
