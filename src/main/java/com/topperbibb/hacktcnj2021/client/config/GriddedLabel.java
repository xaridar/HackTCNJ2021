package com.topperbibb.hacktcnj2021.client.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

class GriddedLabel extends JLabel implements MouseMotionListener, MouseListener {
    private Rectangle selectedArea;
    private int[] firstSelected;
    private SelectionListener listener;
    private double scale;
    private int tileSize;
    private int xPos, yPos;

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
        this.tileSize = size;
        firstSelected = null;
        repaint();
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        repaint();
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
            for (int x = 0; x <= getWidth() / (tileSize * scale); x++) {
                for (int y = 0; y <= getHeight() / (tileSize * scale); y++) {
                    g2d.drawRect((int) (x * tileSize * scale) - 1, (int) (y * tileSize * scale) - 1, (int) (tileSize * scale), (int) (tileSize * scale));
                }
            }
            if (selectedArea != null) {
                g2d.setColor(new Color(0, 0, 255, 50));
                g2d.fillRect((int) (selectedArea.x * tileSize * scale), (int) (selectedArea.y * tileSize * scale), (int) (selectedArea.width * tileSize * scale), (int) (selectedArea.height * tileSize * scale));
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1")) {
            int x = (e.getX() - xPos) / (int) (tileSize * scale);
            int y = (e.getY() - yPos ) / (int) (tileSize * scale);
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
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (firstSelected == null) {
            int x = (e.getX() - xPos) / (int) (tileSize * scale);
            int y = (e.getY() - yPos ) / (int) (tileSize * scale);
            selectedArea = new Rectangle(x, y, 1, 1);
            repaint();
            revalidate();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        firstSelected = null;
        selectedArea = null;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1")) {
            firstSelected = null;
            selectedArea = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (listener != null && selectedArea != null && (selectedArea.width != 1 || selectedArea.height != 1)) {
            listener.onSelect(selectedArea);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1")) {
            firstSelected = null;
        }
    }

    public interface SelectionListener {
        void onSelect(Rectangle selectedArea);
    }
}
