package com.topperbibb.hacktcnj2021.client.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ZoomablePannableLabel extends JLabel implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

    public static final double DEFAULT_SCALE = 1.0;
    public static final double MIN_SCALE = 0.5;
    public static final double MAX_SCALE = 3.0;

    public static final int SIZE = 500;

    public double scale = DEFAULT_SCALE;
    private int xPos = 0, yPos = 0;
    private GriddedLabel imageLabel;

    private Image fullImage = null;
    private Icon currImage;

    private Point lastPoint;

    public ZoomablePannableLabel(ImageIcon image, int horizontalAlignment) {
        super();
        imageLabel = new GriddedLabel(image, horizontalAlignment, scale);
        fullImage = image.getImage();
        currImage = image;
        setLabel();
    }

    public ZoomablePannableLabel(ImageIcon image) {
        super();
        imageLabel = new GriddedLabel(image, scale);
        fullImage = image.getImage();
        currImage = image;
        setLabel();
    }

    public ZoomablePannableLabel() {
        super();
        imageLabel = new GriddedLabel(scale);
        setLabel();
    }

    public void setLabel() {
        setImageLabel();
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setOpaque(true);
        imageLabel.setOpaque(true);
        imageLabel.setSelectionListener(System.out::println);
        setTileSize(20);
    }

    /**
     * Called to update position of image label / redraws it
     */
    private void setImageLabel() {
        imageLabel.setScale(scale);
        imageLabel.setPos(xPos, yPos);
        setPreferredSize(new Dimension(SIZE, SIZE));
        imageLabel.setBackground(Color.WHITE);
        if (currImage != null) imageLabel.setBounds(xPos, yPos, currImage.getIconWidth(), currImage.getIconHeight());
        add(imageLabel);
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon == null) return;
        imageLabel.setIcon(icon);
        if (fullImage == null) {
            fullImage = ((ImageIcon) icon).getImage();
        }
        currImage = icon;
        setImageLabel();
    }

    public void setNewIcon(Icon icon) {
        if (icon == null) return;
        xPos = 0;
        yPos = 0;
        scale = DEFAULT_SCALE;
        fullImage = ((ImageIcon) icon).getImage();
        setIcon(icon);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom(e.getPreciseWheelRotation() / -10, e.getPoint());
    }

    public void zoom(double amt, Point pos) {
        if (currImage == null) return;
        scale += amt;
        scale = (double) Math.round(scale * 10) / 10;
        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));

        // move image to keep percentage
        double perX = ((double) pos.x - xPos) / currImage.getIconWidth();
        double perY = ((double) pos.y - yPos) / currImage.getIconHeight();

        setIcon(new ImageIcon(fullImage.getScaledInstance((int) (fullImage.getWidth(null) * scale), (int) (fullImage.getHeight(null) * scale), 0)));

        int pixelX = (int) (perX * currImage.getIconWidth()) + xPos;
        int pixelY = (int) (perY * currImage.getIconHeight()) + yPos;

        xPos -= pixelX - pos.x;
        yPos -= pixelY - pos.y;

        setImageLabel();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currImage == null) return;
        if (MouseEvent.getMouseModifiersText(e.getModifiersEx()).equals("Button3")) {

            if (lastPoint != null) {
                xPos += e.getPoint().x - lastPoint.x;
                yPos += e.getPoint().y - lastPoint.y;

                if (scale > DEFAULT_SCALE) {
                    xPos = Math.min(0, xPos);
                    yPos = Math.min(0, yPos);
                    if (currImage.getIconWidth() + xPos < SIZE) xPos = SIZE - currImage.getIconWidth();
                    if (currImage.getIconHeight() + yPos < SIZE) yPos = SIZE - currImage.getIconHeight();
                } else if (scale < DEFAULT_SCALE) {
                    xPos = Math.max(0, xPos);
                    yPos = Math.max(0, yPos);
                    if (currImage.getIconWidth() + xPos > SIZE) xPos = SIZE - currImage.getIconWidth();
                    if (currImage.getIconHeight() + yPos > SIZE) yPos = SIZE - currImage.getIconHeight();
                } else {
                    xPos = 0;
                    yPos = 0;
                }

                setImageLabel();
            }

            lastPoint = new Point(e.getPoint());
        }
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
            && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseMoved(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lastPoint = new Point(e.getPoint());
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point(e.getPoint());
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getX() >= imageLabel.getX() && e.getX() <= imageLabel.getX() + imageLabel.getWidth()
                && e.getY() >= imageLabel.getY() && e.getY() <= imageLabel.getY() + imageLabel.getHeight()) {
            imageLabel.mouseExited(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.getModifiersExText(e.getModifiersEx()).equals("Ctrl")) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_0:
                    scale = DEFAULT_SCALE;
                    setIcon(new ImageIcon(fullImage));
                    xPos = 0;
                    yPos = 0;
                    setImageLabel();
                    break;
                case KeyEvent.VK_MINUS:
                    zoom(-0.1, new Point(SIZE / 2, SIZE / 2));
                    break;
                case KeyEvent.VK_EQUALS:
                    zoom(0.1, new Point(SIZE / 2, SIZE / 2));
                    break;
            }
        }
    }

    private void setTileSize(int size) {
        imageLabel.setTileSize(size);
        setImageLabel();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
