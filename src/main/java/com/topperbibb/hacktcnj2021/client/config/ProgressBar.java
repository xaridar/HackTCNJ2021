package com.topperbibb.hacktcnj2021.client.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

public class ProgressBar extends JComponent implements MouseMotionListener, MouseListener {
    private final Color bgColor;
    private final Color fgColor;
    private final Color cursorColor;
    private double progress;
    private final Dimension size;

    private boolean hovered = false;

    private SeekListener seekListener;

    @Override
    public void mouseClicked(MouseEvent e) {
        double percent = (double) (e.getX()) / (size.width - 2);
        if (seekListener != null) {
            if (seekListener.onSeek(percent)) {
                setProgress(percent);
            }
        } else {
            setProgress(percent);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public interface SeekListener {
        boolean onSeek(double progress);

        boolean canSeek();
    }

    public ProgressBar(Color bgColor, Color fgColor, Color cursorColor, Dimension size) {
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.cursorColor = cursorColor;
        this.size = size;

        addMouseMotionListener(this);
        addMouseListener(this);
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

        if (progress > 0) {
            g.setColor(cursorColor);
            g.fillRect((int) (getWidth() * progress - 4), 1, 10, getHeight() - 2);
        }

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
        if (MouseEvent.getModifiersExText(e.getModifiersEx()).equals("Button1") && hovered) {
            double percent = (double) (e.getX()) / (size.width - 2);
            if (seekListener != null) {
                if (seekListener.onSeek(percent)) {
                    setProgress(percent);
                }
            } else {
                setProgress(percent);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() >= progress * getWidth() - 6 && e.getX() <= progress * getWidth() + 4 && seekListener.canSeek()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hovered = true;
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            hovered = false;
        }
    }

    public void setSeekListener(SeekListener seekListener) {
        this.seekListener = seekListener;
    }
}
