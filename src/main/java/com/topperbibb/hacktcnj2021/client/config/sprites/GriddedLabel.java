package com.topperbibb.hacktcnj2021.client.config.sprites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GriddedLabel extends JLabel implements MouseMotionListener, MouseListener {
    private Rectangle selectedArea;
    private int[] firstSelected;
    private SelectionListener listener;
    private double scale;
    public int tileSize;
    private int xPos, yPos;
    public int origHeight;
    public int origWidth;
    private int newHeight;
    private int newWidth;
    private boolean dragged;

    public GriddedLabel(String text, Icon icon, int horizontalAlignment, double scale) {
        super(text, icon, horizontalAlignment);
        setLayout(null);
        this.scale = scale;
    }

    public GriddedLabel(String text, int horizontalAlignment, double scale) {
        super(text, horizontalAlignment);
        setLayout(null);
        this.scale = scale;
    }

    public GriddedLabel(String text, double scale) {
        super(text);
        setLayout(null);
        this.scale = scale;
    }

    public GriddedLabel(Icon image, int horizontalAlignment, double scale) {
        super(image, horizontalAlignment);
        setLayout(null);
        this.scale = scale;
    }

    public GriddedLabel(Icon image, double scale) {
        super(image);
        setLayout(null);
        this.scale = scale;
    }

    public GriddedLabel(double scale) {
        super();
        setLayout(null);
        this.scale = scale;
    }

    public void setTileSize(int size) {
        if (size == 0) return;
        if (origWidth % size != 0 || origHeight % size != 0) this.tileSize = 0;
        else this.tileSize = size / 2;
        firstSelected = null;
        repaint();
    }

    public void setScale(double scale) {
        this.scale = scale;
        setBorder(BorderFactory.createLineBorder(Color.black));
        repaint();
    }

    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        repaint();
    }

    public void setDims(int width, int height, int newHeight, int newWidth) {
        this.origWidth = width;
        this.origHeight = height;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        firstSelected = null;
        dragged = false;
    }

    public void setSelected(Rectangle selected) {
        selectedArea = new Rectangle(selected.x, selected.y, selected.width, selected.height);
        dragged = selected.width > 0 && selected.height > 0;
    }

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (tileSize * scale > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.black);
            for (int x = 0; x <= origWidth / tileSize; x++) {
                g2d.drawLine((int) (x * newWidth * tileSize * scale / origWidth), 0, (int) (x * newWidth * tileSize * scale / origWidth), getHeight());
            }
            for (int y = 0; y <= origHeight / tileSize; y++) {
                g2d.drawLine(0, (int) (y * newHeight * tileSize * scale / origHeight), getWidth(), (int) (y * newHeight * tileSize * scale / origHeight));
            }
            if (selectedArea != null) {
                g2d.setColor(new Color(0, 0, 255, 50));
                g2d.fillRect((int) (selectedArea.x * newWidth * tileSize * scale / origWidth) + 1,
                        (int) (selectedArea.y * newHeight * tileSize * scale / origHeight) + 1,
                        (int) (selectedArea.width * newWidth * tileSize * scale / origWidth),
                        (int) (selectedArea.height * newHeight * tileSize * scale / origHeight));
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1") && (int) (tileSize * scale) > 0) {
            int x = (int) ((e.getX() - xPos) / (newWidth * tileSize * scale / origWidth));
            int y = (int) ((e.getY() - yPos ) / (newHeight * tileSize * scale / origHeight));
            if (firstSelected == null) {
                firstSelected = new int[]{x, y};
                selectedArea = new Rectangle(x, y, 1, 1);
            } else {
                int drawX;
                int drawY;
                int drawW;
                int drawH;
                if (firstSelected[0] < x) {
                    drawX = firstSelected[0];
                    drawW = x - firstSelected[0] + 1;
                } else {
                    drawX = x;
                    drawW = firstSelected[0] - x + 1;
                }
                if (firstSelected[1] < y) {
                    drawY = firstSelected[1];
                    drawH = y - firstSelected[1] + 1;
                } else {
                    drawY = y;
                    drawH = firstSelected[1] - y + 1;
                }
                selectedArea = new Rectangle(drawX, drawY, drawW, drawH);
            }
            repaint();
            revalidate();
            dragged = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (firstSelected == null && (int) (tileSize * scale) > 0 && !dragged) {
            int x = (int) ((e.getX() - xPos) / (newWidth * tileSize * scale / origWidth));
            int y = (int) ((e.getY() - yPos ) / (newHeight * tileSize * scale / origHeight));
            selectedArea = new Rectangle(x, y, 1, 1);
            repaint();
            revalidate();
            dragged = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        firstSelected = null;
        selectedArea = null;
        dragged = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1 && tileSize * scale > 0) {
            firstSelected = null;
            selectedArea = null;
            dragged = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1 && listener != null && selectedArea != null && tileSize * scale > 0) {
            listener.onSelect(selectedArea);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public interface SelectionListener {
        void onSelect(Rectangle selectedArea);
    }
}
