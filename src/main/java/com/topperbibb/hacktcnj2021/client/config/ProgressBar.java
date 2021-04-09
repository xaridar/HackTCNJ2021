package com.topperbibb.hacktcnj2021.client.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

public class ProgressBar extends JComponent implements MouseMotionListener {
    private final Color bgColor;
    private final Color fgColor;
    private double progress;
    private final Dimension size;

    public ProgressBar(Color bgColor, Color fgColor, Dimension size) {
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.size = size;

        addMouseMotionListener(this);
        progress = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(bgColor);
        RoundRectangle2D roundRect = new RoundRectangle2D.Float();
        g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

        roundRect.setRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
        g.setClip(roundRect);
        g.setColor(fgColor);
        g.fillRect(1, 1, (int) (getWidth() * progress) - 2, getHeight() - 2);

        g.setClip(new Rectangle(0, 0, getWidth(), getHeight()));
        g.setColor(Color.BLACK);
        g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
    }

    public void setProgress(double progress) {
        this.progress = progress;
        repaint();
    }

    public void addProgress(double progress) {
        this.progress += progress;
    }

    public void removeProgress(double progress) {
        this.progress -= progress;
    }

    public double getProgress() {
        return progress;
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1")) {
            double percent = (double) (e.getX() - getX()) / getWidth();
            setProgress(percent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
